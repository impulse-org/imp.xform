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

import org.eclipse.core.resources.IFile;
import org.eclipse.imp.xform.XformPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class ASTSearchResult extends AbstractTextSearchResult implements IFileMatchAdapter, IEditorMatchAdapter {
    private ASTSearchQuery fQuery;

    public ASTSearchResult(ASTSearchQuery query) {
	fQuery= query;
    }

    public IEditorMatchAdapter getEditorMatchAdapter() {
	return this;
    }

    public IFileMatchAdapter getFileMatchAdapter() {
	return this;
    }

    public Match[] computeContainedMatches(AbstractTextSearchResult result, IFile file) {
	return getMatches(file);
    }

    public IFile getFile(Object element) {
	return (IFile) element;
    }

    public boolean isShownInEditor(Match match, IEditorPart editor) {
        IEditorInput editorInput= editor.getEditorInput();

        if (editorInput instanceof IFileEditorInput)  {
            IFileEditorInput fileEditorInput= (IFileEditorInput) editorInput;
            IFile file= fileEditorInput.getFile();
            IFile matchFile= (IFile) match.getElement();

            return (file.equals(matchFile));
        }
        return false;
    }

    public Match[] computeContainedMatches(AbstractTextSearchResult result, IEditorPart editor) {
        IEditorInput editorInput= editor.getEditorInput();

        if (editorInput instanceof IFileEditorInput)  {
            IFileEditorInput fileEditorInput= (IFileEditorInput) editorInput;

            return computeContainedMatches(result, fileEditorInput.getFile());
        }
        return new Match[0];
    }

    public String getLabel() {
	return fQuery.getLabel();
    }

    public String getTooltip() {
	return "Heh?";
    }

    public ISearchQuery getQuery() {
	return fQuery;
    }

    public ImageDescriptor getImageDescriptor() {
	return XformPlugin.getImageDescriptor("icons/astSearchHit.gif");
    }
}
