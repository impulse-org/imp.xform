
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
 *<li>Rule 12:  PatternList ::= Pattern
 *<li>Rule 13:  PatternList ::= PatternList Pattern
 *</b>
 */
public class PatternList_PatternList extends PatternList
{
    private ASTPatternParser environment;
    public ASTPatternParser getEnvironment() { return environment; }

    public PatternList_PatternList(ASTPatternParser environment, IToken leftIToken, IToken rightIToken, boolean leftRecursive)
    {
        super(leftIToken, rightIToken, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public PatternList_PatternList(ASTPatternParser environment, IPattern _Pattern, boolean leftRecursive)
    {
        super(_Pattern, leftRecursive);
        this.environment = environment;
        initialize();
    }

    public void add(IPattern _Pattern)
    {
        super.add((PatternNode) _Pattern);
    }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof PatternList_PatternList)) return false;
        if (! super.equals(o)) return false;
        PatternList_PatternList other = (PatternList_PatternList) o;
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

        public PatternList betaSubst(Map bindings) {
            PatternList mappedPatterns= new PatternList_PatternList(environment, leftIToken, rightIToken, true);
            for(int i=0; i < size(); i++) {
                IPattern pattern= getPatternAt(i);
                // Following instanceof's wouldn't be necessary if JikesPG could promote common production methods to the non-terminal interface.
                IPattern mappedPattern= null;
                if (pattern instanceof Pattern)
                    mappedPattern= ((Pattern) pattern).betaSubst(bindings);
                else if (pattern instanceof Node)
                    mappedPattern= ((Node) pattern).betaSubst(bindings);
                else if (pattern instanceof FunctionCall)
                    mappedPattern= ((FunctionCall) pattern).betaSubst(bindings);
                mappedPatterns.add(mappedPattern);
            }
            return mappedPatterns;
        }
     }


