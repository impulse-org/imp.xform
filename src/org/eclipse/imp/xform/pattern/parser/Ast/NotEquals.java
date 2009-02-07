
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

import org.eclipse.imp.xform.pattern.parser.*;
import lpg.runtime.*;

import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import org.eclipse.imp.services.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
/**
 *<b>
 *<li>Rule 53:  NotEquals ::= !=$
 *</b>
 */
public class NotEquals extends PatternNodeToken implements INotEquals
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    public NotEquals(ASTPatternParser environment, IToken token)    {
        super(token);
        this.environment = environment;
        initialize();
    }

    public void accept(IAstVisitor v)
    {
        if (! v.preVisit(this)) return;
        enter((Visitor) v);
        v.postVisit(this);
    }

    public void enter(Visitor v)
    {
        v.visit(this);
        v.endVisit(this);
    }
 public boolean evaluate(Object lhs, Object rhs, Object node) {
           // Oh well, can't put a method on a non-terminal interface, so fake the polymorphism here
           Object lhsValue= lhs, rhsValue= rhs;
           if (lhs instanceof NodeAttribute)
             lhsValue= ((NodeAttribute) lhs).getValue(node);
           else if (lhs instanceof StringLiteral)
             lhsValue= ((StringLiteral) lhs).getValue();
           else if (lhs instanceof NumberLiteral)
             lhsValue= ((NumberLiteral) lhs).getValue();
           if (rhs instanceof NodeAttribute)
             rhsValue= ((NodeAttribute) rhs).getValue(node);
           else if (rhs instanceof StringLiteral)
             rhsValue= ((StringLiteral) rhs).getValue();
           else if (rhs instanceof NumberLiteral)
             rhsValue= ((NumberLiteral) rhs).getValue();
           // If either side has no value, let the comparison fail, since the user may write
           // a node type constraint that is somewhat loose (e.g. "[Expr e { name == 'x' }]"),
           // and so attributes may be requested that don't actually exist for a given node.
           if (lhsValue == null || rhsValue == null)
             return false;
           return !lhsValue.equals(rhsValue);
       }
    }


