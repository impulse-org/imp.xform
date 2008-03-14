/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package org.eclipse.imp.xform.search;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.language.Language;
import org.eclipse.imp.language.LanguageRegistry;
import org.eclipse.imp.language.ServiceFactory;
import org.eclipse.imp.model.ISourceProject;
import org.eclipse.imp.model.ModelFactory;
import org.eclipse.imp.model.ModelFactory.ModelException;
import org.eclipse.imp.parser.IMessageHandler;
import org.eclipse.imp.parser.IParseController;
import org.eclipse.imp.utils.StreamUtils;
import org.eclipse.imp.xform.XformPlugin;
import org.eclipse.imp.xform.pattern.matching.IASTMatcher;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.parser.ASTPatternLexer;
import org.eclipse.imp.xform.pattern.parser.ASTPatternParser;
import org.eclipse.imp.xform.pattern.parser.Ast.Pattern;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.Match;

public class ASTSearchQuery implements ISearchQuery {
    private String fASTPatternString;

    private String fLanguageName;

    private Language fLanguage;

    private Pattern fASTPattern;

    private IASTMatcher fASTAdapter;

    private ASTSearchScope fScope;

    private ASTSearchResult fResult;

    public ASTSearchQuery(String astPattern, String languageName, ASTSearchScope scope) {
	fASTPatternString= astPattern;
	fLanguageName= languageName;
	fLanguage= LanguageRegistry.findLanguage(fLanguageName);
	fScope= scope;
        fResult= new ASTSearchResult(this);

	fASTAdapter= (IASTMatcher) ServiceFactory.getInstance().getASTAdapter(fLanguage);
	ASTPatternParser.setASTAdapter(fASTAdapter);

	ASTPatternLexer lexer= new ASTPatternLexer(fASTPatternString.toCharArray(), "__PATTERN__");
        ASTPatternParser parser= new ASTPatternParser(lexer.getLexStream());
    
        lexer.lexer(parser); // Why wasn't this done by the parser ctor?
	fASTPattern= (Pattern) parser.parser();
    }

    class SystemOutMessageHandler implements IMessageHandler {
	public void endMessageGroup() { }

	public void handleSimpleMessage(String msg, int startOffset, int endOffset, int startCol, int endCol, int startLine, int endLine) {
            System.out.println("[" + startOffset + ":" + (endOffset - startOffset + 1) + "] " + msg);
	}

	public void startMessageGroup(String groupName) { }
    }

    public IStatus run(final IProgressMonitor monitor) throws OperationCanceledException {
	final IMessageHandler msgHandler= new SystemOutMessageHandler();

	for(Iterator iter= fScope.getProjects().iterator(); iter.hasNext(); ) {
            final IProject p= (IProject) iter.next();

            try {
                p.accept(new IResourceVisitor() {
                    public boolean visit(IResource resource) throws CoreException {
                        if (resource instanceof IFile) {
                            IFile file= (IFile) resource;
                            String exten= file.getFileExtension();

                            if (resource.isDerived())
                        	System.out.println("Skipping derived resource " + resource.getFullPath());

                            if (exten != null && fLanguage.hasExtension(exten)) {
                        	monitor.subTask("Searching " + file.getFullPath());
                                String contents= StreamUtils.readStreamContents(file.getContents(), ResourcesPlugin.getEncoding());
                                IParseController parseController= ServiceFactory.getInstance().getParseController(fLanguage);
                                ISourceProject srcProject;
				try {
				    srcProject= ModelFactory.open(p);
				} catch (ModelException e) {
				    ErrorHandler.reportError(e.getMessage());
				    throw new CoreException(new Status(IStatus.ERROR, XformPlugin.kPluginID, 0, e.getMessage(), e));
				}

                                if (parseController == null)
                                    return false;
                                parseController.initialize(resource.getProjectRelativePath(), srcProject, msgHandler);

                                Object astRoot= parseController.parse(contents, false, monitor);

                                if (astRoot == null)
                                    return false;

                                Matcher m= new Matcher(fASTPattern);
                                Set/*MatchResult*/ matches= fASTAdapter.findAllMatches(m, astRoot);

                                for(Iterator iterator= matches.iterator(); iterator.hasNext(); ) {
				    MatchResult match= (MatchResult) iterator.next();
				    Object matchNode= match.getMatchNode();
                                    Match textMatch= new Match(file, fASTAdapter.getOffset(matchNode), fASTAdapter.getLength(matchNode));

                                    fResult.addMatch(textMatch);
				}
                            }
                            return false;
                        }
                        return true;
                    }
                });
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return new Status(IStatus.OK, "org.eclipse.imp.xform", 0, "Search complete", null);
    }

    public String getLabel() {
	return "<AST " + fASTPatternString + ">";
    }

    public boolean canRerun() {
	return false;
    }

    public boolean canRunInBackground() {
	return true;
    }

    public ISearchResult getSearchResult() {
	return fResult;
    }
}
