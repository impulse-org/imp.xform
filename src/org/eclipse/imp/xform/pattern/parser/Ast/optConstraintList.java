
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
 *<em>
 *<li>Rule 27:  optConstraintList ::= $Empty
 *</em>
 *<p>
 *<b>
 *<li>Rule 28:  optConstraintList ::= {$ ConstraintList }$
 *</b>
 */
public class optConstraintList extends PatternNode implements IoptConstraintList
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private ConstraintList_ConstraintList _ConstraintList;

    public ConstraintList_ConstraintList getConstraintList() { return _ConstraintList; }

    public optConstraintList(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                             ConstraintList_ConstraintList _ConstraintList)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._ConstraintList = _ConstraintList;
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
        if (! (o instanceof optConstraintList)) return false;
        optConstraintList other = (optConstraintList) o;
        if (! _ConstraintList.equals(other._ConstraintList)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_ConstraintList.hashCode());
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
            _ConstraintList.accept(v);
        v.endVisit(this);
    }

        public optConstraintList betaSubst(Map bindings) {
            return new optConstraintList(environment, leftIToken, rightIToken,
                                         (ConstraintList_ConstraintList) _ConstraintList.betaSubst(bindings));
        }
     }


