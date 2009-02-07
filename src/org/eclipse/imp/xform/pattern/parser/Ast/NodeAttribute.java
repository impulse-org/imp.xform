
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
 *<li>Rule 42:  NodeAttribute ::= optAttrList IDENT
 *</b>
 */
public class NodeAttribute extends PatternNode implements INodeAttribute
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    private optAttrList_identList _optAttrList;
    private PatternNodeToken _IDENT;

    public optAttrList_identList getoptAttrList() { return _optAttrList; }
    public PatternNodeToken getIDENT() { return _IDENT; }

    public NodeAttribute(ASTPatternParser environment, IToken leftIToken, IToken rightIToken,
                         optAttrList_identList _optAttrList,
                         PatternNodeToken _IDENT)
    {
        super(leftIToken, rightIToken);

        this.environment = environment;
        this._optAttrList = _optAttrList;
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
        if (! (o instanceof NodeAttribute)) return false;
        NodeAttribute other = (NodeAttribute) o;
        if (! _optAttrList.equals(other._optAttrList)) return false;
        if (! _IDENT.equals(other._IDENT)) return false;
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = hash * 31 + (_optAttrList.hashCode());
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
        {
            _optAttrList.accept(v);
            _IDENT.accept(v);
        }
        v.endVisit(this);
    }
 public Object getValue(Object targetNode) {
           for(int i=0; i < _optAttrList.size(); i++)
               targetNode= environment.getASTAdapter().getValue(_optAttrList.getElementAt(i).toString(), targetNode);
           return environment.getASTAdapter().getValue(_IDENT.toString(), targetNode);
       }
       public NodeAttribute betaSubst(Map bindings) {
           return new NodeAttribute(environment, leftIToken, rightIToken,
                                    (optAttrList_identList) _optAttrList.betaSubst(bindings),
                                    _IDENT);
       }
     }


