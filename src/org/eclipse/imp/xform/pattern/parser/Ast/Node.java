
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
 *<li>Rule 15:  Node ::= FunctionCall
 *</em>
 *<p>
 *<b>
 *<li>Rule 14:  Node ::= [$ NodeType$type optNodeName$name optSharp optTargetType$targetType optConstraintList$constraints ChildList ]$
 *</b>
 */
public class Node extends PatternNode implements INode
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private NodeType _type;
    private optNodeName _name;
    private optSharp _optSharp;
    private optTargetType _targetType;
    private optConstraintList _constraints;
    private ChildList_ChildList _ChildList;

    public NodeType gettype() { return _type; }
    /**
     * The value returned by <b>getname</b> may be <b>null</b>
     */
    public optNodeName getname() { return _name; }
    /**
     * The value returned by <b>getoptSharp</b> may be <b>null</b>
     */
    public optSharp getoptSharp() { return _optSharp; }
    /**
     * The value returned by <b>gettargetType</b> may be <b>null</b>
     */
    public optTargetType gettargetType() { return _targetType; }
    /**
     * The value returned by <b>getconstraints</b> may be <b>null</b>
     */
    public optConstraintList getconstraints() { return _constraints; }
    public ChildList_ChildList getChildList() { return _ChildList; }

    public Node(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                NodeType _type,
                optNodeName _name,
                optSharp _optSharp,
                optTargetType _targetType,
                optConstraintList _constraints,
                ChildList_ChildList _ChildList)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._type = _type;
        this._name = _name;
        this._optSharp = _optSharp;
        this._targetType = _targetType;
        this._constraints = _constraints;
        this._ChildList = _ChildList;
        initialize();
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof Node)) return false;
        if (! super.equals(o)) return false;
        Node other = (Node) o;
        if (! _type.equals(other._type)) return false;
        if (_name == null)
            if (other._name != null) return false;
            else; // continue
        else if (! _name.equals(other._name)) return false;
        if (_optSharp == null)
            if (other._optSharp != null) return false;
            else; // continue
        else if (! _optSharp.equals(other._optSharp)) return false;
        if (_targetType == null)
            if (other._targetType != null) return false;
            else; // continue
        else if (! _targetType.equals(other._targetType)) return false;
        if (_constraints == null)
            if (other._constraints != null) return false;
            else; // continue
        else if (! _constraints.equals(other._constraints)) return false;
        if (! _ChildList.equals(other._ChildList)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        hash = hash * 31 + (_type.hashCode());
        hash = hash * 31 + (_name == null ? 0 : _name.hashCode());
        hash = hash * 31 + (_optSharp == null ? 0 : _optSharp.hashCode());
        hash = hash * 31 + (_targetType == null ? 0 : _targetType.hashCode());
        hash = hash * 31 + (_constraints == null ? 0 : _constraints.hashCode());
        hash = hash * 31 + (_ChildList.hashCode());
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
            _type.accept(v);
            if (_name != null) _name.accept(v);
            if (_optSharp != null) _optSharp.accept(v);
            if (_targetType != null) _targetType.accept(v);
            if (_constraints != null) _constraints.accept(v);
            _ChildList.accept(v);
        }
        v.endVisit(this);
    }

         public Node betaSubst(Map bindings) {
             optNodeName name= _name;
             if (name != null) {
                 IToken mappedName= bindings.containsKey(_name) ? (IToken) bindings.get(_name) : _name.getIDENT();

                 name= new optNodeName(mappedName);
             }
             return new Node(environment, leftIToken, rightIToken, _type, name,
	                _optSharp, _targetType,
	                _constraints.betaSubst(bindings),
	                (ChildList_ChildList) _ChildList.betaSubst(bindings));
         }
      }


