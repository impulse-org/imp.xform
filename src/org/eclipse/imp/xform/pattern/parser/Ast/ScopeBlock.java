
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
 *<li>Rule 11:  ScopeBlock ::= {$ PatternList }$
 *</b>
 */
public class ScopeBlock extends PatternNode implements IScopeBlock
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private PatternList_PatternList _PatternList;

    public PatternList_PatternList getPatternList() { return _PatternList; }

    public ScopeBlock(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                      PatternList_PatternList _PatternList)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._PatternList = _PatternList;
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
        if (! (o instanceof ScopeBlock)) return false;
        ScopeBlock other = (ScopeBlock) o;
        if (! _PatternList.equals(other._PatternList)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_PatternList.hashCode());
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
            _PatternList.accept(v);
        v.endVisit(this);
    }

        public ScopeBlock betaSubst(Map bindings) {
            PatternList_PatternList mappedPatterns= (PatternList_PatternList) _PatternList.betaSubst(bindings);
            return new ScopeBlock(environment, leftIToken, rightIToken, mappedPatterns);
        }
     }


