
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
 *<li>Rule 43:  optAttrList ::= $Empty
 *<li>Rule 44:  optAttrList ::= optAttrList ident .$
 *</b>
 */
public class identList extends AbstractPatternNodeList implements IoptAttrList
{
    public ident getidentAt(int i) { return (ident) getElementAt(i); }

    public identList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
    }

    public identList(ident _ident, boolean leftRecursive)
    {
        super((PatternNode) _ident, leftRecursive);
    }

    public void add(ident _ident)
    {
        super.add((PatternNode) _ident);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof identList)) return false;
        if (! super.equals(o)) return false;
        identList other = (identList) o;
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
}


