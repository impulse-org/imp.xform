
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
 *<li>Rule 17:  ActualArgList ::= ActualArg
 *<li>Rule 18:  ActualArgList ::= ActualArgList ,$ ActualArg
 *</b>
 */
public class ActualArgList extends AbstractPatternNodeList implements IActualArgList
{
    public ActualArg getActualArgAt(int i) { return (ActualArg) getElementAt(i); }

    public ActualArgList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
    }

    public ActualArgList(ActualArg _ActualArg, boolean leftRecursive)
    {
        super((PatternNode) _ActualArg, leftRecursive);
    }

    public void add(ActualArg _ActualArg)
    {
        super.add((PatternNode) _ActualArg);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof ActualArgList)) return false;
        if (! super.equals(o)) return false;
        ActualArgList other = (ActualArgList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            ActualArg element = getActualArgAt(i);
            if (! element.equals(other.getActualArgAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getActualArgAt(i).hashCode());
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
                ActualArg element = getActualArgAt(i);
                if (! v.preVisit(element)) continue;
                element.enter(v);
                v.postVisit(element);
            }
        }
        v.endVisit(this);
    }
}


