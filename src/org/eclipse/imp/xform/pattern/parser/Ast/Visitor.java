
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
public interface Visitor extends IAstVisitor
{
    boolean visit(PatternNode n);
    void endVisit(PatternNode n);

    boolean visit(PatternNodeToken n);
    void endVisit(PatternNodeToken n);

    boolean visit(FunctionDef n);
    void endVisit(FunctionDef n);

    boolean visit(FormalArgList n);
    void endVisit(FormalArgList n);

    boolean visit(FormalArg n);
    void endVisit(FormalArg n);

    boolean visit(RewriteRule n);
    void endVisit(RewriteRule n);

    boolean visit(Pattern n);
    void endVisit(Pattern n);

    boolean visit(ScopeBlock n);
    void endVisit(ScopeBlock n);

    boolean visit(PatternList_PatternList n);
    void endVisit(PatternList_PatternList n);

    boolean visit(PatternList n);
    void endVisit(PatternList n);

    boolean visit(Node n);
    void endVisit(Node n);

    boolean visit(FunctionCall n);
    void endVisit(FunctionCall n);

    boolean visit(ActualArgList_ActualArgList n);
    void endVisit(ActualArgList_ActualArgList n);

    boolean visit(ActualArgList n);
    void endVisit(ActualArgList n);

    boolean visit(ActualArg n);
    void endVisit(ActualArg n);

    boolean visit(optSharp n);
    void endVisit(optSharp n);

    boolean visit(NodeType n);
    void endVisit(NodeType n);

    boolean visit(optNodeName n);
    void endVisit(optNodeName n);

    boolean visit(optTargetType n);
    void endVisit(optTargetType n);

    boolean visit(optConstraintList n);
    void endVisit(optConstraintList n);

    boolean visit(ConstraintList_ConstraintList n);
    void endVisit(ConstraintList_ConstraintList n);

    boolean visit(ConstraintList n);
    void endVisit(ConstraintList n);

    boolean visit(OperatorConstraint n);
    void endVisit(OperatorConstraint n);

    boolean visit(BoundConstraint n);
    void endVisit(BoundConstraint n);

    boolean visit(NumericBound n);
    void endVisit(NumericBound n);

    boolean visit(Unbounded n);
    void endVisit(Unbounded n);

    boolean visit(NodeAttribute n);
    void endVisit(NodeAttribute n);

    boolean visit(optAttrList_identList n);
    void endVisit(optAttrList_identList n);

    boolean visit(identList n);
    void endVisit(identList n);

    boolean visit(ident n);
    void endVisit(ident n);

    boolean visit(NumberLiteral n);
    void endVisit(NumberLiteral n);

    boolean visit(StringLiteral n);
    void endVisit(StringLiteral n);

    boolean visit(Equals n);
    void endVisit(Equals n);

    boolean visit(NotEquals n);
    void endVisit(NotEquals n);

    boolean visit(ChildList_ChildList n);
    void endVisit(ChildList_ChildList n);

    boolean visit(ChildList n);
    void endVisit(ChildList n);

    boolean visit(Child n);
    void endVisit(Child n);

    boolean visit(ClosureLink n);
    void endVisit(ClosureLink n);

    boolean visit(DirectLink0 n);
    void endVisit(DirectLink0 n);

    boolean visit(DirectLink1 n);
    void endVisit(DirectLink1 n);

}


