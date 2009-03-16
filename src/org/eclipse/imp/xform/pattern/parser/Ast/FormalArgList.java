
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
 *<li>Rule 5:  FormalArgList ::= FormalArg
 *<li>Rule 6:  FormalArgList ::= FormalArgList ,$ FormalArg
 *</b>
 */
public class FormalArgList extends AbstractPatternNodeList implements IFormalArgList
{
    public FormalArg getFormalArgAt(int i) { return (FormalArg) getElementAt(i); }

    public FormalArgList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
    }

    public FormalArgList(FormalArg _FormalArg, boolean leftRecursive)
    {
        super((PatternNode) _FormalArg, leftRecursive);
    }

    public void add(FormalArg _FormalArg)
    {
        super.add((PatternNode) _FormalArg);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof FormalArgList)) return false;
        if (! super.equals(o)) return false;
        FormalArgList other = (FormalArgList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            FormalArg element = getFormalArgAt(i);
            if (! element.equals(other.getFormalArgAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getFormalArgAt(i).hashCode());
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
                FormalArg element = getFormalArgAt(i);
                if (! v.preVisit(element)) continue;
                element.enter(v);
                v.postVisit(element);
            }
        }
        v.endVisit(this);
    }
}


