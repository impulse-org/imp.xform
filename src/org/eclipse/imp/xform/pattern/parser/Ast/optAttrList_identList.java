
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
 *<li>Rule 43:  optAttrList ::= $Empty
 *<li>Rule 44:  optAttrList ::= optAttrList ident .$
 *</b>
 */
public class optAttrList_identList extends identList
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    public optAttrList_identList(ASTPatternParser environment, IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public optAttrList_identList(ASTPatternParser environment, ident _ident, boolean leftRecursive)
    {
        super(_ident, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public void add(ident _ident)
    {
        super.add((PatternNode) _ident);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof optAttrList_identList)) return false;
        if (! super.equals(o)) return false;
        optAttrList_identList other = (optAttrList_identList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            ident element = getidentAt(i);
            if (! element.equals(other.getidentAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getidentAt(i).hashCode());
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
            for (int i = 0; i < size(); i++)
            {
                ident element = getidentAt(i);
                if (! v.preVisit(element)) continue;
                element.enter(v);
                v.postVisit(element);
            }
        }
        v.endVisit(this);
    }

        public identList betaSubst(Map bindings) {
            // Is it right to map each component individually? Probably not...
            identList mappedIdents= new optAttrList_identList(environment, leftIToken, rightIToken, true);
            for(int i=0; i < size(); i++) {
                ident id= getidentAt(i);
                mappedIdents.add(bindings.containsKey(id) ? (ident) bindings.get(id) : id);
            }
            return mappedIdents;
        }
     }


