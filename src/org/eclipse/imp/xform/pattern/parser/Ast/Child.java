
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
 *<li>Rule 56:  Child ::= LinkType Node
 *</b>
 */
public class Child extends PatternNode implements IChild
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private ILinkType _LinkType;
    private INode _Node;

    /**
     * The value returned by <b>getLinkType</b> may be <b>null</b>
     */
    public ILinkType getLinkType() { return _LinkType; }
    public INode getNode() { return _Node; }

    public Child(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                 ILinkType _LinkType,
                 INode _Node)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._LinkType = _LinkType;
        this._Node = _Node;
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
        if (! (o instanceof Child)) return false;
        Child other = (Child) o;
        if (_LinkType == null)
            if (other._LinkType != null) return false;
            else; // continue
        else if (! _LinkType.equals(other._LinkType)) return false;
        if (! _Node.equals(other._Node)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_LinkType == null ? 0 : _LinkType.hashCode());
        hash = hash * 31 + (_Node.hashCode());
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
            if (_LinkType != null) _LinkType.accept(v);
            _Node.accept(v);
        }
        v.endVisit(this);
    }

        public Child betaSubst(Map bindings) {
            INode mappedNode= null;
            if (_Node instanceof Node)
                mappedNode= ((Node) _Node).betaSubst(bindings);
            else if (_Node instanceof FunctionCall)
                mappedNode= ((FunctionCall) _Node).betaSubst(bindings);
            return new Child(environment, leftIToken, rightIToken, _LinkType, mappedNode);
        }
    }


