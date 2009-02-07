
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
 *<li>Rule 62:  ClosureLink ::= DirectLink ... -
 *</b>
 */
public class ClosureLink extends PatternNode implements IClosureLink
{
    private IDirectLink _DirectLink;
    private PatternNodeToken _ELLIPSIS;
    private PatternNodeToken _MINUS;

    /**
     * The value returned by <b>getDirectLink</b> may be <b>null</b>
     */
    public IDirectLink getDirectLink() { return _DirectLink; }
    public PatternNodeToken getELLIPSIS() { return _ELLIPSIS; }
    public PatternNodeToken getMINUS() { return _MINUS; }

    public ClosureLink(IToken leftIToken, IToken rightIToken,
                       IDirectLink _DirectLink,
                       PatternNodeToken _ELLIPSIS,
                       PatternNodeToken _MINUS)
    {
        super(leftIToken, rightIToken);

        this._DirectLink = _DirectLink;
        this._ELLIPSIS = _ELLIPSIS;
        this._MINUS = _MINUS;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        //
        // The super call test is not required for now because an Ast node
        // can only extend the root Ast, AstToken and AstList and none of
        // these nodes contain additional children.
        //
        // if (! super.equals(o)) return false;
        //
        if (! (o instanceof ClosureLink)) return false;
        ClosureLink other = (ClosureLink) o;
        if (_DirectLink == null)
            if (other._DirectLink != null) return false;
            else; // continue
        else if (! _DirectLink.equals(other._DirectLink)) return false;
        if (! _ELLIPSIS.equals(other._ELLIPSIS)) return false;
        if (! _MINUS.equals(other._MINUS)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_DirectLink == null ? 0 : _DirectLink.hashCode());
        hash = hash * 31 + (_ELLIPSIS.hashCode());
        hash = hash * 31 + (_MINUS.hashCode());
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
            if (_DirectLink != null) _DirectLink.accept(v);
            _ELLIPSIS.accept(v);
            _MINUS.accept(v);
        }
        v.endVisit(this);
    }
}


