
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
 *<li>Rule 4:  FunctionDef ::= DEFINE$ IDENT ($ FormalArgList )$ {$ Pattern$Body }$
 *</b>
 */
public class FunctionDef extends PatternNode implements IFunctionDef
{
    private PatternNodeToken _IDENT;
    private FormalArgList _FormalArgList;
    private IPattern _Body;

    public PatternNodeToken getIDENT() { return _IDENT; }
    public FormalArgList getFormalArgList() { return _FormalArgList; }
    public IPattern getBody() { return _Body; }

    public FunctionDef(IToken leftIToken, IToken rightIToken,
                       PatternNodeToken _IDENT,
                       FormalArgList _FormalArgList,
                       IPattern _Body)
    {
        super(leftIToken, rightIToken);

        this._IDENT = _IDENT;
        this._FormalArgList = _FormalArgList;
        this._Body = _Body;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof FunctionDef)) return false;
        if (! super.equals(o)) return false;
        FunctionDef other = (FunctionDef) o;
        if (! _IDENT.equals(other._IDENT)) return false;
        if (! _FormalArgList.equals(other._FormalArgList)) return false;
        if (! _Body.equals(other._Body)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_IDENT.hashCode());
        hash = hash * 31 + (_FormalArgList.hashCode());
        hash = hash * 31 + (_Body.hashCode());
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
            _IDENT.accept(v);
            _FormalArgList.accept(v);
            _Body.accept(v);
        }
        v.endVisit(this);
    }
}


