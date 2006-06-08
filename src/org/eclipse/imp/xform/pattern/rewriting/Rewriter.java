/*
 * Created on Jun 8, 2006
 */
package com.ibm.watson.safari.xform.pattern.rewriting;

import com.ibm.watson.safari.xform.pattern.matching.IASTAdapter;
import com.ibm.watson.safari.xform.pattern.matching.MatchResult;
import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.parser.Ast.Pattern;

public class Rewriter {
    private final IASTAdapter fAdapter;

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
