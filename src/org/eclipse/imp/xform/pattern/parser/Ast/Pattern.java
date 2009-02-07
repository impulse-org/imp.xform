
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
 *<li>Rule 9:  Pattern ::= Node
 *<li>Rule 10:  Pattern ::= Node ScopeBlock
 *</b>
 */
public class Pattern extends PatternNode implements IPattern
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private INode _Node;
    private ScopeBlock _ScopeBlock;

    public INode getNode() { return _Node; }
    /**
     * The value returned by <b>getScopeBlock</b> may be <b>null</b>
     */
    public ScopeBlock getScopeBlock() { return _ScopeBlock; }

    public Pattern(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                   INode _Node,
                   ScopeBlock _ScopeBlock)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._Node = _Node;
        this._ScopeBlock = _ScopeBlock;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof Pattern)) return false;
        if (! super.equals(o)) return false;
        Pattern other = (Pattern) o;
        if (! _Node.equals(other._Node)) return false;
        if (_ScopeBlock == null)
            if (other._ScopeBlock != null) return false;
            else; // continue
        else if (! _ScopeBlock.equals(other._ScopeBlock)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_Node.hashCode());
        hash = hash * 31 + (_ScopeBlock == null ? 0 : _ScopeBlock.hashCode());
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
            _Node.accept(v);
            if (_ScopeBlock != null) _ScopeBlock.accept(v);
        }
        v.endVisit(this);
    }

    public Pattern betaSubst(Map bindings) {
        if (bindings.isEmpty())
            return this;
        INode node= ((Node) _Node).betaSubst(bindings);
        ScopeBlock scope= (_ScopeBlock == null) ? null : _ScopeBlock.betaSubst(bindings);

        return new Pattern(environment, leftIToken, rightIToken, node, scope);
    }
 }


