
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
 *<li>Rule 16:  FunctionCall ::= IDENT ($ ActualArgList )$
 *</b>
 */
public class FunctionCall extends PatternNode implements IFunctionCall
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private PatternNodeToken _IDENT;
    private ActualArgList_ActualArgList _ActualArgList;

    public PatternNodeToken getIDENT() { return _IDENT; }
    public ActualArgList_ActualArgList getActualArgList() { return _ActualArgList; }

    public FunctionCall(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                        PatternNodeToken _IDENT,
                        ActualArgList_ActualArgList _ActualArgList)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._IDENT = _IDENT;
        this._ActualArgList = _ActualArgList;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof FunctionCall)) return false;
        if (! super.equals(o)) return false;
        FunctionCall other = (FunctionCall) o;
        if (! _IDENT.equals(other._IDENT)) return false;
        if (! _ActualArgList.equals(other._ActualArgList)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_IDENT.hashCode());
        hash = hash * 31 + (_ActualArgList.hashCode());
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
            _ActualArgList.accept(v);
        }
        v.endVisit(this);
    }

         public FunctionCall betaSubst(Map bindings) {
             return new FunctionCall(environment, leftIToken, rightIToken, _IDENT,
	                (ActualArgList_ActualArgList) _ActualArgList.betaSubst(bindings));
         }
     }


