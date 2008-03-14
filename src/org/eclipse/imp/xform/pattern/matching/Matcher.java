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

package org.eclipse.imp.xform.pattern.matching;

import org.eclipse.imp.services.IASTAdapter;
import org.eclipse.imp.xform.pattern.parser.ASTPatternParser;
import org.eclipse.imp.xform.pattern.parser.Ast.*;

/**
 * @author rfuhrer@watson.ibm.com
 */
public class Matcher {
    private Pattern fPattern;

    private final IASTAdapter fASTAdapter= ASTPatternParser.getASTAdapter();
    private final ASTPatternParser.SymbolTable fSymbolTable= ASTPatternParser.getSymbolTable();

    public Matcher(Pattern p) {
        fPattern= p;
    }

    public MatchResult match(Object ast) {
	if (fPattern == null)
	    return null;
        try {
            MatchResult m= new MatchResult(ast);

            if (doMatch(fPattern.getNode(), ast, m))
                return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean doMatch(IPattern pattern, Object astNode, MatchResult match) throws Exception {
	INode node= null;
	ScopeBlock block= null;

	if (pattern instanceof Pattern) {
	    Pattern p= (Pattern) pattern;
	    block= p.getScopeBlock();
	    node= p.getNode();
	} else if (pattern instanceof FunctionCall || pattern instanceof Node) {
	    node= (INode) pattern;
	}

	if (node instanceof FunctionCall) {
	    FunctionCall call= (FunctionCall) node;
	    PatternNodeToken name= call.getIDENT();
	    FunctionDef def= fSymbolTable.lookup(name.toString());

	    ActualArgList actuals= call.getActualArgList();
	    FormalArgList formals= def.getFormalArgList();

	    if (actuals.size() != formals.size())
		throw new IllegalArgumentException("Wrong # of arguments " + actuals.size() + " to pattern function " + name + " (expected " + formals.size() + ").");

	    IPattern body= def.getBody();

	} else if (node instanceof Node) {
	    Node patternNode= (Node) node;
            NodeType patNodeASTType= patternNode.gettype();
            optTargetType patNodeTargetType= patternNode.gettargetType();
            String typeName= patNodeASTType.getIDENT().toString();

            if (patNodeASTType != null && !fASTAdapter.isInstanceOfType(astNode, typeName))
                return false;
            if (patNodeTargetType != null && !patNodeTargetType.getIDENT().toString().equals(fASTAdapter.getValue(IASTAdapter.TARGET_TYPE, astNode)))
                return false;
            if (!checkConstraints(patternNode, astNode))
                return false;

            ChildList patChildren= patternNode.getChildList();

            if (patChildren.size() > 0) {
                // Don't ask the AST adapter for getChildren() unless the pattern actually has child constraints.
                Object[] astChildren= fASTAdapter.getChildren(astNode);

                for(int i= 0; i < patChildren.size(); i++) {
            	Child patChild= patChildren.getChildAt(i);

            	int j= 0;
            	for(; j < astChildren.length; j++) {
            	    if (doMatch(patChild.getNode(), astChildren[j], match))
            		break;
            	}
            	if (j == astChildren.length)
            	    return false;
                }
            }
            if (patternNode.getname() != null)
                match.addBinding(patternNode.getname().getIDENT().toString(), astNode);
        }
        match.fMatchNode= astNode;
        return true;
    }

    private boolean checkConstraints(Node patternNode, Object astNode) throws Exception {
        optConstraintList optConstraints= patternNode.getconstraints();

        if (optConstraints != null) {
            ConstraintList constraints= optConstraints.getConstraintList();

            for(int i=0; i < constraints.size(); i++) {
                IConstraint constraint= constraints.getConstraintAt(i);

                if (constraint instanceof OperatorConstraint) {
                    OperatorConstraint cons= (OperatorConstraint) constraint;
                    IOperator op= cons.getOperator();

                    // TODO would be nice if IOperator.evaluate() existed, but there
                    // is currently no way to annotate the JikesPG grammar to do that.
                    if (op instanceof Equals) {
                        if (!((Equals) op).evaluate(cons.getlhs(), cons.getrhs(), astNode))
                            return false;
                    } else if (op instanceof NotEquals) {
                        if (!((NotEquals) op).evaluate(cons.getlhs(), cons.getrhs(), astNode))
                            return false;
                    } else {
                        throw new Exception("Unable to evaluate operator: " + op.toString());
                    }
                } else if (constraint instanceof BoundConstraint) {
//                    BoundConstraint cons= (BoundConstraint) constraint;
//                    IBound lb= cons.getlowerBound();
//                    IBound ub= cons.getupperBound();
                }
            }
        }
        return true;
    }

    public String toString() {
        return "<matcher: " + fPattern + ">";
    }

    public IASTAdapter getAdapter() {
	return fASTAdapter;
    }
}
