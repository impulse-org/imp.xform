
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
public abstract class PatternNode implements IAst
{
    public IAst getNextAst() { return null; }
    protected IToken leftIToken,
                     rightIToken;
    public IAst getParent()
    {
        throw new UnsupportedOperationException("noparent-saved option in effect");
    }

    public IToken getLeftIToken() { return leftIToken; }
    public IToken getRightIToken() { return rightIToken; }
    public IToken[] getPrecedingAdjuncts() { return leftIToken.getPrecedingAdjuncts(); }
    public IToken[] getFollowingAdjuncts() { return rightIToken.getFollowingAdjuncts(); }

    public String toString()
    {
        return leftIToken.getPrsStream().toString(leftIToken, rightIToken);
    }

    public PatternNode(IToken token) { this.leftIToken = this.rightIToken = token; }
    public PatternNode(IToken leftIToken, IToken rightIToken)
    {
        this.leftIToken = leftIToken;
        this.rightIToken = rightIToken;
    }

    void initialize() {}

    public java.util.ArrayList getChildren()
    {
        throw new UnsupportedOperationException("noparent-saved option in effect");
    }
    public java.util.ArrayList getAllChildren() { return getChildren(); }

    /**
     * Since the Ast type has no children, any two instances of it are equal.
     */
    public boolean equals(Object o) { return o instanceof PatternNode; }
    public abstract int hashCode();
    public abstract void accept(IAstVisitor v);
}


