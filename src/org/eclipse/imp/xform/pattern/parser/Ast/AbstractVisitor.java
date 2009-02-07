
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2007 IBM Corporation.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
//Contributors:
//    Philippe Charles (pcharles@us.ibm.com) - initial API and implementation

////////////////////////////////////////////////////////////////////////////////

package org.eclipse.imp.xform.pattern.parser.Ast;

import lpg.runtime.*;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import org.eclipse.imp.services.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
public abstract class AbstractVisitor implements Visitor
{
    public abstract void unimplementedVisitor(String s);

    public boolean preVisit(IAst element) { return true; }

    public void postVisit(IAst element) {}

    public boolean visit(PatternNodeToken n) { unimplementedVisitor("visit(PatternNodeToken)"); return true; }
    public void endVisit(PatternNodeToken n) { unimplementedVisitor("endVisit(PatternNodeToken)"); }

    public boolean visit(FunctionDef n) { unimplementedVisitor("visit(FunctionDef)"); return true; }
    public void endVisit(FunctionDef n) { unimplementedVisitor("endVisit(FunctionDef)"); }

    public boolean visit(FormalArgList n) { unimplementedVisitor("visit(FormalArgList)"); return true; }
    public void endVisit(FormalArgList n) { unimplementedVisitor("endVisit(FormalArgList)"); }

    public boolean visit(FormalArg n) { unimplementedVisitor("visit(FormalArg)"); return true; }
    public void endVisit(FormalArg n) { unimplementedVisitor("endVisit(FormalArg)"); }

    public boolean visit(RewriteRule n) { unimplementedVisitor("visit(RewriteRule)"); return true; }
    public void endVisit(RewriteRule n) { unimplementedVisitor("endVisit(RewriteRule)"); }

    public boolean visit(Pattern n) { unimplementedVisitor("visit(Pattern)"); return true; }
    public void endVisit(Pattern n) { unimplementedVisitor("endVisit(Pattern)"); }

    public boolean visit(ScopeBlock n) { unimplementedVisitor("visit(ScopeBlock)"); return true; }
    public void endVisit(ScopeBlock n) { unimplementedVisitor("endVisit(ScopeBlock)"); }

    public boolean visit(PatternList_PatternList n) { unimplementedVisitor("visit(PatternList_PatternList)"); return true; }
    public void endVisit(PatternList_PatternList n) { unimplementedVisitor("endVisit(PatternList_PatternList)"); }

    public boolean visit(PatternList n) { unimplementedVisitor("visit(PatternList)"); return true; }
    public void endVisit(PatternList n) { unimplementedVisitor("endVisit(PatternList)"); }

    public boolean visit(Node n) { unimplementedVisitor("visit(Node)"); return true; }
    public void endVisit(Node n) { unimplementedVisitor("endVisit(Node)"); }

    public boolean visit(FunctionCall n) { unimplementedVisitor("visit(FunctionCall)"); return true; }
    public void endVisit(FunctionCall n) { unimplementedVisitor("endVisit(FunctionCall)"); }

    public boolean visit(ActualArgList_ActualArgList n) { unimplementedVisitor("visit(ActualArgList_ActualArgList)"); return true; }
    public void endVisit(ActualArgList_ActualArgList n) { unimplementedVisitor("endVisit(ActualArgList_ActualArgList)"); }

    public boolean visit(ActualArgList n) { unimplementedVisitor("visit(ActualArgList)"); return true; }
    public void endVisit(ActualArgList n) { unimplementedVisitor("endVisit(ActualArgList)"); }

    public boolean visit(ActualArg n) { unimplementedVisitor("visit(ActualArg)"); return true; }
    public void endVisit(ActualArg n) { unimplementedVisitor("endVisit(ActualArg)"); }

    public boolean visit(optSharp n) { unimplementedVisitor("visit(optSharp)"); return true; }
    public void endVisit(optSharp n) { unimplementedVisitor("endVisit(optSharp)"); }

    public boolean visit(NodeType n) { unimplementedVisitor("visit(NodeType)"); return true; }
    public void endVisit(NodeType n) { unimplementedVisitor("endVisit(NodeType)"); }

    public boolean visit(optNodeName n) { unimplementedVisitor("visit(optNodeName)"); return true; }
    public void endVisit(optNodeName n) { unimplementedVisitor("endVisit(optNodeName)"); }

    public boolean visit(optTargetType n) { unimplementedVisitor("visit(optTargetType)"); return true; }
    public void endVisit(optTargetType n) { unimplementedVisitor("endVisit(optTargetType)"); }

    public boolean visit(optConstraintList n) { unimplementedVisitor("visit(optConstraintList)"); return true; }
    public void endVisit(optConstraintList n) { unimplementedVisitor("endVisit(optConstraintList)"); }

    public boolean visit(ConstraintList_ConstraintList n) { unimplementedVisitor("visit(ConstraintList_ConstraintList)"); return true; }
    public void endVisit(ConstraintList_ConstraintList n) { unimplementedVisitor("endVisit(ConstraintList_ConstraintList)"); }

    public boolean visit(ConstraintList n) { unimplementedVisitor("visit(ConstraintList)"); return true; }
    public void endVisit(ConstraintList n) { unimplementedVisitor("endVisit(ConstraintList)"); }

    public boolean visit(OperatorConstraint n) { unimplementedVisitor("visit(OperatorConstraint)"); return true; }
    public void endVisit(OperatorConstraint n) { unimplementedVisitor("endVisit(OperatorConstraint)"); }

    public boolean visit(BoundConstraint n) { unimplementedVisitor("visit(BoundConstraint)"); return true; }
    public void endVisit(BoundConstraint n) { unimplementedVisitor("endVisit(BoundConstraint)"); }

    public boolean visit(NumericBound n) { unimplementedVisitor("visit(NumericBound)"); return true; }
    public void endVisit(NumericBound n) { unimplementedVisitor("endVisit(NumericBound)"); }

    public boolean visit(Unbounded n) { unimplementedVisitor("visit(Unbounded)"); return true; }
    public void endVisit(Unbounded n) { unimplementedVisitor("endVisit(Unbounded)"); }

    public boolean visit(NodeAttribute n) { unimplementedVisitor("visit(NodeAttribute)"); return true; }
    public void endVisit(NodeAttribute n) { unimplementedVisitor("endVisit(NodeAttribute)"); }

    public boolean visit(optAttrList_identList n) { unimplementedVisitor("visit(optAttrList_identList)"); return true; }
    public void endVisit(optAttrList_identList n) { unimplementedVisitor("endVisit(optAttrList_identList)"); }

    public boolean visit(identList n) { unimplementedVisitor("visit(identList)"); return true; }
    public void endVisit(identList n) { unimplementedVisitor("endVisit(identList)"); }

    public boolean visit(ident n) { unimplementedVisitor("visit(ident)"); return true; }
    public void endVisit(ident n) { unimplementedVisitor("endVisit(ident)"); }

    public boolean visit(NumberLiteral n) { unimplementedVisitor("visit(NumberLiteral)"); return true; }
    public void endVisit(NumberLiteral n) { unimplementedVisitor("endVisit(NumberLiteral)"); }

    public boolean visit(StringLiteral n) { unimplementedVisitor("visit(StringLiteral)"); return true; }
    public void endVisit(StringLiteral n) { unimplementedVisitor("endVisit(StringLiteral)"); }

    public boolean visit(Equals n) { unimplementedVisitor("visit(Equals)"); return true; }
    public void endVisit(Equals n) { unimplementedVisitor("endVisit(Equals)"); }

    public boolean visit(NotEquals n) { unimplementedVisitor("visit(NotEquals)"); return true; }
    public void endVisit(NotEquals n) { unimplementedVisitor("endVisit(NotEquals)"); }

    public boolean visit(ChildList_ChildList n) { unimplementedVisitor("visit(ChildList_ChildList)"); return true; }
    public void endVisit(ChildList_ChildList n) { unimplementedVisitor("endVisit(ChildList_ChildList)"); }

    public boolean visit(ChildList n) { unimplementedVisitor("visit(ChildList)"); return true; }
    public void endVisit(ChildList n) { unimplementedVisitor("endVisit(ChildList)"); }

    public boolean visit(Child n) { unimplementedVisitor("visit(Child)"); return true; }
    public void endVisit(Child n) { unimplementedVisitor("endVisit(Child)"); }

    public boolean visit(ClosureLink n) { unimplementedVisitor("visit(ClosureLink)"); return true; }
    public void endVisit(ClosureLink n) { unimplementedVisitor("endVisit(ClosureLink)"); }

    public boolean visit(DirectLink0 n) { unimplementedVisitor("visit(DirectLink0)"); return true; }
    public void endVisit(DirectLink0 n) { unimplementedVisitor("endVisit(DirectLink0)"); }

    public boolean visit(DirectLink1 n) { unimplementedVisitor("visit(DirectLink1)"); return true; }
    public void endVisit(DirectLink1 n) { unimplementedVisitor("endVisit(DirectLink1)"); }


    public boolean visit(PatternNode n)
    {
        if (n instanceof PatternNodeToken) return visit((PatternNodeToken) n);
        else if (n instanceof FunctionDef) return visit((FunctionDef) n);
        else if (n instanceof FormalArgList) return visit((FormalArgList) n);
        else if (n instanceof FormalArg) return visit((FormalArg) n);
        else if (n instanceof RewriteRule) return visit((RewriteRule) n);
        else if (n instanceof Pattern) return visit((Pattern) n);
        else if (n instanceof ScopeBlock) return visit((ScopeBlock) n);
        else if (n instanceof PatternList_PatternList) return visit((PatternList_PatternList) n);
        else if (n instanceof PatternList) return visit((PatternList) n);
        else if (n instanceof Node) return visit((Node) n);
        else if (n instanceof FunctionCall) return visit((FunctionCall) n);
        else if (n instanceof ActualArgList_ActualArgList) return visit((ActualArgList_ActualArgList) n);
        else if (n instanceof ActualArgList) return visit((ActualArgList) n);
        else if (n instanceof ActualArg) return visit((ActualArg) n);
        else if (n instanceof optSharp) return visit((optSharp) n);
        else if (n instanceof NodeType) return visit((NodeType) n);
        else if (n instanceof optNodeName) return visit((optNodeName) n);
        else if (n instanceof optTargetType) return visit((optTargetType) n);
        else if (n instanceof optConstraintList) return visit((optConstraintList) n);
        else if (n instanceof ConstraintList_ConstraintList) return visit((ConstraintList_ConstraintList) n);
        else if (n instanceof ConstraintList) return visit((ConstraintList) n);
        else if (n instanceof OperatorConstraint) return visit((OperatorConstraint) n);
        else if (n instanceof BoundConstraint) return visit((BoundConstraint) n);
        else if (n instanceof NumericBound) return visit((NumericBound) n);
        else if (n instanceof Unbounded) return visit((Unbounded) n);
        else if (n instanceof NodeAttribute) return visit((NodeAttribute) n);
        else if (n instanceof optAttrList_identList) return visit((optAttrList_identList) n);
        else if (n instanceof identList) return visit((identList) n);
        else if (n instanceof ident) return visit((ident) n);
        else if (n instanceof NumberLiteral) return visit((NumberLiteral) n);
        else if (n instanceof StringLiteral) return visit((StringLiteral) n);
        else if (n instanceof Equals) return visit((Equals) n);
        else if (n instanceof NotEquals) return visit((NotEquals) n);
        else if (n instanceof ChildList_ChildList) return visit((ChildList_ChildList) n);
        else if (n instanceof ChildList) return visit((ChildList) n);
        else if (n instanceof Child) return visit((Child) n);
        else if (n instanceof ClosureLink) return visit((ClosureLink) n);
        else if (n instanceof DirectLink0) return visit((DirectLink0) n);
        else if (n instanceof DirectLink1) return visit((DirectLink1) n);
        throw new UnsupportedOperationException("visit(" + n.getClass().toString() + ")");
    }
    public void endVisit(PatternNode n)
    {
        if (n instanceof PatternNodeToken) endVisit((PatternNodeToken) n);
        else if (n instanceof FunctionDef) endVisit((FunctionDef) n);
        else if (n instanceof FormalArgList) endVisit((FormalArgList) n);
        else if (n instanceof FormalArg) endVisit((FormalArg) n);
        else if (n instanceof RewriteRule) endVisit((RewriteRule) n);
        else if (n instanceof Pattern) endVisit((Pattern) n);
        else if (n instanceof ScopeBlock) endVisit((ScopeBlock) n);
        else if (n instanceof PatternList_PatternList) endVisit((PatternList_PatternList) n);
        else if (n instanceof PatternList) endVisit((PatternList) n);
        else if (n instanceof Node) endVisit((Node) n);
        else if (n instanceof FunctionCall) endVisit((FunctionCall) n);
        else if (n instanceof ActualArgList_ActualArgList) endVisit((ActualArgList_ActualArgList) n);
        else if (n instanceof ActualArgList) endVisit((ActualArgList) n);
        else if (n instanceof ActualArg) endVisit((ActualArg) n);
        else if (n instanceof optSharp) endVisit((optSharp) n);
        else if (n instanceof NodeType) endVisit((NodeType) n);
        else if (n instanceof optNodeName) endVisit((optNodeName) n);
        else if (n instanceof optTargetType) endVisit((optTargetType) n);
        else if (n instanceof optConstraintList) endVisit((optConstraintList) n);
        else if (n instanceof ConstraintList_ConstraintList) endVisit((ConstraintList_ConstraintList) n);
        else if (n instanceof ConstraintList) endVisit((ConstraintList) n);
        else if (n instanceof OperatorConstraint) endVisit((OperatorConstraint) n);
        else if (n instanceof BoundConstraint) endVisit((BoundConstraint) n);
        else if (n instanceof NumericBound) endVisit((NumericBound) n);
        else if (n instanceof Unbounded) endVisit((Unbounded) n);
        else if (n instanceof NodeAttribute) endVisit((NodeAttribute) n);
        else if (n instanceof optAttrList_identList) endVisit((optAttrList_identList) n);
        else if (n instanceof identList) endVisit((identList) n);
        else if (n instanceof ident) endVisit((ident) n);
        else if (n instanceof NumberLiteral) endVisit((NumberLiteral) n);
        else if (n instanceof StringLiteral) endVisit((StringLiteral) n);
        else if (n instanceof Equals) endVisit((Equals) n);
        else if (n instanceof NotEquals) endVisit((NotEquals) n);
        else if (n instanceof ChildList_ChildList) endVisit((ChildList_ChildList) n);
        else if (n instanceof ChildList) endVisit((ChildList) n);
        else if (n instanceof Child) endVisit((Child) n);
        else if (n instanceof ClosureLink) endVisit((ClosureLink) n);
        else if (n instanceof DirectLink0) endVisit((DirectLink0) n);
        else if (n instanceof DirectLink1) endVisit((DirectLink1) n);
        throw new UnsupportedOperationException("visit(" + n.getClass().toString() + ")");
    }
}

