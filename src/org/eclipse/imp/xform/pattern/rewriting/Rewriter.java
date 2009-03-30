/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

/*
 * Created on Jun 8, 2006
 */
package org.eclipse.imp.xform.pattern.rewriting;

import org.eclipse.imp.services.IASTMatchAdapter;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.parser.Ast.Pattern;

public class Rewriter {
    private final IASTMatchAdapter fAdapter;

    private final Pattern fRuleRHS;

    private final Matcher fMatcher;

    public Rewriter(Matcher matcher, Pattern rhs) {
	fMatcher= matcher;
	fRuleRHS= rhs;
	fAdapter= fMatcher.getAdapter();
    }

    public Object rewrite(MatchResult result) {
	return result.getMatchNode();
    }
}
