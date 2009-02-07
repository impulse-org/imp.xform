
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
 *<em>
 *<li>Rule 26:  optTargetType ::= $Empty
 *</em>
 *<p>
 *<b>
 *<li>Rule 25:  optTargetType ::= COLON$ IDENT
 *</b>
 */
public class optTargetType extends PatternNode implements IoptTargetType
{
    private PatternNodeToken _IDENT;

    public PatternNodeToken getIDENT() { return _IDENT; }

    public optTargetType(IToken leftIToken, IToken rightIToken,
                         PatternNodeToken _IDENT)
    {
        super(leftIToken, rightIToken);

        this._IDENT = _IDENT;
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
        if (! (o instanceof optTargetType)) return false;
        optTargetType other = (optTargetType) o;
        if (! _IDENT.equals(other._IDENT)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_IDENT.hashCode());
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
            _IDENT.accept(v);
        v.endVisit(this);
    }
}


