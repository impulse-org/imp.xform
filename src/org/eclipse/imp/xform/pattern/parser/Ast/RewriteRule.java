
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
/**
 *<b>
 *<li>Rule 8:  RewriteRule ::= Pattern$lhs =>$ Pattern$rhs
 *</b>
 */
public class RewriteRule extends PatternNode implements IRewriteRule
{
    private IPattern _lhs;
    private IPattern _rhs;

    public IPattern getlhs() { return _lhs; }
    public IPattern getrhs() { return _rhs; }

    public RewriteRule(IToken leftIToken, IToken rightIToken,
                       IPattern _lhs,
                       IPattern _rhs)
    {
        super(leftIToken, rightIToken);

        this._lhs = _lhs;
        this._rhs = _rhs;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof RewriteRule)) return false;
        if (! super.equals(o)) return false;
        RewriteRule other = (RewriteRule) o;
        if (! _lhs.equals(other._lhs)) return false;
        if (! _rhs.equals(other._rhs)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_lhs.hashCode());
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
            _rhs.accept(v);
        }
        v.endVisit(this);
    }
}


