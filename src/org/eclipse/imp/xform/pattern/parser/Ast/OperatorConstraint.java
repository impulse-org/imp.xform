
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
 *<li>Rule 33:  OperatorConstraint ::= NodeAttribute$lhs Operator Value$rhs
 *</b>
 */
public class OperatorConstraint extends PatternNode implements IOperatorConstraint
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private NodeAttribute _lhs;
    private IOperator _Operator;
    private IValue _rhs;

    public NodeAttribute getlhs() { return _lhs; }
    public IOperator getOperator() { return _Operator; }
    public IValue getrhs() { return _rhs; }

    public OperatorConstraint(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                              NodeAttribute _lhs,
                              IOperator _Operator,
                              IValue _rhs)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._lhs = _lhs;
        this._Operator = _Operator;
        this._rhs = _rhs;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof OperatorConstraint)) return false;
        if (! super.equals(o)) return false;
        OperatorConstraint other = (OperatorConstraint) o;
        if (! _lhs.equals(other._lhs)) return false;
        if (! _Operator.equals(other._Operator)) return false;
        if (! _rhs.equals(other._rhs)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_lhs.hashCode());
        hash = hash * 31 + (_Operator.hashCode());
        hash = hash * 31 + (_rhs.hashCode());
        return hash;
    }

    public void accept(IAstVisitor v)
    {
        if (! v.preVisit(this)) return;
        enter((Visitor) v);
        v.postVisit(this);
    }

    public void enter(Visitor v)
    {
        boolean checkChildren = v.visit(this);
        if (checkChildren)
        {
            _lhs.accept(v);
            _Operator.accept(v);
            _rhs.accept(v);
        }
        v.endVisit(this);
    }

        public OperatorConstraint betaSubst(Map bindings) {
            IValue newRHS= null;
            if (_rhs instanceof NodeAttribute)
                newRHS= ((NodeAttribute) _rhs).betaSubst(bindings);
            else if (_rhs instanceof ILiteral)
                newRHS= _rhs;
            else if (_rhs instanceof Node)
                newRHS= ((Node) _rhs).betaSubst(bindings);
            return new OperatorConstraint(environment, leftIToken, rightIToken,
                                          _lhs.betaSubst(bindings),
                                          _Operator,
                                          newRHS);
        }
     }


