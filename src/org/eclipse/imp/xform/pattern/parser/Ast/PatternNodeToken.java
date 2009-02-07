
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
public class PatternNodeToken extends PatternNode implements IPatternNodeToken
{
    public PatternNodeToken(IToken token) { super(token); }
    public IToken getIToken() { return leftIToken; }
    public String toString() { return leftIToken.toString(); }

    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (! (o instanceof PatternNodeToken)) return false;
        PatternNodeToken other = (PatternNodeToken) o;
        return toString().equals(other.toString());
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public void accept(IAstVisitor v)
    {
        if (! v.preVisit(this)) return;
        enter((Visitor) v);
        v.postVisit(this);
    }

    public void enter(Visitor v)
    {
        v.visit(this);
        v.endVisit(this);
    }
}


