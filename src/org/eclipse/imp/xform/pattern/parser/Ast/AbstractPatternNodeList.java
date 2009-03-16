
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
public abstract class AbstractPatternNodeList extends PatternNode
{
    private boolean leftRecursive;
    private java.util.ArrayList list;
    public int size() { return list.size(); }
    public PatternNode getElementAt(int i) { return (PatternNode) list.get(leftRecursive ? i : list.size() - 1 - i); }
    public java.util.ArrayList getArrayList()
    {
        if (! leftRecursive) // reverse the list 
        {
            for (int i = 0, n = list.size() - 1; i < n; i++, n--)
            {
                Object ith = list.get(i),
                       nth = list.get(n);
                list.set(i, nth);
                list.set(n, ith);
            }
            leftRecursive = true;
        }
        return list;
    }
    public void add(PatternNode element)
    {
        list.add(element);
        if (leftRecursive)
             rightIToken = element.getRightIToken();
        else leftIToken = element.getLeftIToken();
    }

    public AbstractPatternNodeList(IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken);
        this.leftRecursive = leftRecursive;
        list = new java.util.ArrayList();
    }

    public AbstractPatternNodeList(PatternNode element, boolean leftRecursive)
    {
        this(element.getLeftIToken(), element.getRightIToken(), leftRecursive);
        list.add(element);
    }

}


