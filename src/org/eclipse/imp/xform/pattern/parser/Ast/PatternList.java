
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
 *<li>Rule 12:  PatternList ::= Pattern
 *<li>Rule 13:  PatternList ::= PatternList Pattern
 *</b>
 */
public class PatternList extends AbstractPatternNodeList implements IPatternList
{
    public IPattern getPatternAt(int i) { return (IPattern) getElementAt(i); }

    public PatternList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
    }

    public PatternList(IPattern _Pattern, boolean leftRecursive)
    {
        super((PatternNode) _Pattern, leftRecursive);
    }

    public void add(IPattern _Pattern)
    {
        super.add((PatternNode) _Pattern);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof PatternList)) return false;
        if (! super.equals(o)) return false;
        PatternList other = (PatternList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            IPattern element = getPatternAt(i);
            if (! element.equals(other.getPatternAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getPatternAt(i).hashCode());
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
                IPattern element = getPatternAt(i);
                element.accept(v);
            }
        }
        v.endVisit(this);
    }
}


