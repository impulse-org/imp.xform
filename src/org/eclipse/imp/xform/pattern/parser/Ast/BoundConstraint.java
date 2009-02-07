
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
 *<li>Rule 34:  BoundConstraint ::= <$ Bound$lowerBound :$ Bound$upperBound >$
 *</b>
 */
public class BoundConstraint extends PatternNode implements IBoundConstraint
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private IBound _lowerBound;
    private IBound _upperBound;

    public IBound getlowerBound() { return _lowerBound; }
    public IBound getupperBound() { return _upperBound; }

    public BoundConstraint(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                           IBound _lowerBound,
                           IBound _upperBound)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._lowerBound = _lowerBound;
        this._upperBound = _upperBound;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof BoundConstraint)) return false;
        if (! super.equals(o)) return false;
        BoundConstraint other = (BoundConstraint) o;
        if (! _lowerBound.equals(other._lowerBound)) return false;
        if (! _upperBound.equals(other._upperBound)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_lowerBound.hashCode());
        hash = hash * 31 + (_upperBound.hashCode());
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
            _lowerBound.accept(v);
            _upperBound.accept(v);
        }
        v.endVisit(this);
    }

        public BoundConstraint betaSubst(Map bindings) {
            return this;
        }
     }


