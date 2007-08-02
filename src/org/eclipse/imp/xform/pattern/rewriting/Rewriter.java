/*
 * Created on Jun 8, 2006
 */
package org.eclipse.imp.xform.pattern.rewriting;

import org.eclipse.imp.xform.pattern.matching.IASTAdapter;
import org.eclipse.imp.xform.pattern.matching.MatchResult;
import org.eclipse.imp.xform.pattern.matching.Matcher;
import org.eclipse.imp.xform.pattern.parser.Ast.Pattern;

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
