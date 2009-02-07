
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
 *<li>Rule 54:  ChildList ::= $Empty
 *<li>Rule 55:  ChildList ::= ChildList Child
 *</b>
 */
public class ChildList_ChildList extends ChildList
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    public ChildList_ChildList(ASTPatternParser environment, IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public ChildList_ChildList(ASTPatternParser environment, Child _Child, boolean leftRecursive)
    {
        super(_Child, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public void add(Child _Child)
    {
        super.add((PatternNode) _Child);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof ChildList_ChildList)) return false;
        ChildList_ChildList other = (ChildList_ChildList) o;
        if (size() != other.size()) return false;
        for (int i = 0; i < size(); i++)
        {
            Child element = getChildAt(i);
            if (! element.equals(other.getChildAt(i))) return false;
        }
        return true;
    }

    public int hashCode()
    {
        int hash = 7;
        for (int i = 0; i < size(); i++)
            hash = hash * 31 + (getChildAt(i).hashCode());
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
                Child element = getChildAt(i);
                if (! v.preVisit(element)) continue;
                element.enter(v);
                v.postVisit(element);
            }
        }
        v.endVisit(this);
    }

        public ChildList betaSubst(Map bindings) {
            ChildList newList= new ChildList_ChildList(environment, leftIToken, rightIToken, true);
            for(int i=0; i < size(); i++) {
                newList.add(getChildAt(i).betaSubst(bindings));
            }
            return newList;
        }
    }


