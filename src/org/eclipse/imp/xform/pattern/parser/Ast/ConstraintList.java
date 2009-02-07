
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
 *<li>Rule 29:  ConstraintList ::= Constraint
 *<li>Rule 30:  ConstraintList ::= ConstraintList ,$ Constraint
 *</b>
 */
public class ConstraintList extends AbstractPatternNodeList implements IConstraintList
{
    public IConstraint getConstraintAt(int i) { return (IConstraint) getElementAt(i); }

    public ConstraintList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
    }

    public ConstraintList(IConstraint _Constraint, boolean leftRecursive)
    {
        super((PatternNode) _Constraint, leftRecursive);
    }

    public void add(IConstraint _Constraint)
    {
        super.add((PatternNode) _Constraint);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof ConstraintList)) return false;
        if (! super.equals(o)) return false;
        ConstraintList other = (ConstraintList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            IConstraint element = getConstraintAt(i);
            if (! element.equals(other.getConstraintAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = super.hashCode();
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getConstraintAt(i).hashCode());
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
                IConstraint element = getConstraintAt(i);
                element.accept(v);
            }
        }
        v.endVisit(this);
    }
}


