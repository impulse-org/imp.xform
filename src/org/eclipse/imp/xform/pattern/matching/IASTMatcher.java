package org.eclipse.imp.xform.pattern.matching;

import java.util.Set;

import org.eclipse.imp.services.IASTAdapter;

public interface IASTMatcher extends IASTAdapter {
    /**
     * This is essentially a wrapper for the AST traversal surrounding the matching
     * process. For each node, the implementation should call <code>matcher.match()</code>
     * to actually perform the match.
     * @param matcher
     * @param astRoot
     * @return the set of non-null MatchContext's, if any, returned by <code>matcher.match()</code>
     * for all matching nodes in the AST rooted at astRoot
     */
    public Set findAllMatches(Matcher matcher, Object astRoot);

    /**
     * Finds the next match using the given Matcher occurring after the given offset.<br>
     * This is essentially a wrapper for the AST traversal surrounding the matching
     * process. For each node, the implementation should call <code>matcher.match()</code>
     * to actually perform the match.
     * @param matcher the matcher to use (encapsulates the pattern)
     * @param astRoot the top of the AST tree to search
     * @param startPos the character at which to begin the search
     * @return the first non-null MatchContext returned by <code>matcher.match()</code>, if any;
     * otherwise, null
     */
    public MatchResult findNextMatch(Matcher matcher, Object astRoot, int offset);
}
