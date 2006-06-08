package com.ibm.watson.safari.xform.pattern.matching;

import java.util.Map;
import java.util.Set;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

/**
 * @author rfuhrer@watson.ibm.com
 */
public interface IASTAdapter {
    public static final String TARGET_TYPE= "targetType";

    public static final String NODE_KIND= "nodeKind";

    /**
     * @return the value of the given attribute for the given AST node
     */
    public Object getValue(NodeAttribute attribute, Object astNode);

    /**
     * @return the value of the named attribute for the given AST node
     */
    public Object getValue(String attributeName, Object astNode);

    /**
     * @return a possibly empty array of immediate children of the given AST node <code>astNode</code>
     */
    public Object[] getChildren(Object astNode);

    /**
     * @return fully-qualified name of the type with the given simple name
     */
    public String lookupSimpleNodeType(String simpleName);

    /**
     * @return true iff the given AST node is of the given named type, which
     * must be fully-qualified
     */
    public boolean isInstanceOfType(Object astNode, String typeName);

    /**
     * @return true iff the given AST node type is a sub-type of the given named
     * type <code>maybeSub</code>, which must be fully-qualified
     */
    public boolean isSubTypeOf(String maybeSuper, String maybeSub);

    /**
     * @return the type of AST node for the given AST node, which is suitable
     * for use with, e.g., isInstanceOfType()
     */
    public String getTypeOf(Object astNode);

    /**
     * @param qualName the fully-qualified name of the AST node type to construct
     * @param children array of child AST nodes (even if certain "children" may
     * not be AST nodes in the target AST representation)
     * @return newly-constructed AST node
     */
    public Object construct(String qualName, Object[] children) throws IllegalArgumentException;

    /**
     * @param qualName the fully-qualified name of the AST node type to construct
     * @param children array of child AST nodes (even if certain "children" may
     * not be AST nodes in the target AST representation)
     * @param attribs map from attribute names to attribute values
     * @return newly-constructed AST node
     */
    public Object construct(String qualName, Object[] children, Map/*<String,Object>*/ attribs) throws IllegalArgumentException;

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

    /**
     * Finds the previous match using the given Matcher occurring before the given offset.<br>
     * This is essentially a wrapper for the AST traversal surrounding the matching
     * process. For each node, the implementation should call <code>matcher.match()</code>
     * to actually perform the match.
     * @param matcher the matcher to use (encapsulates the pattern)
     * @param astRoot the top of the AST tree to search
     * @param startPos the character at which to begin the search
     * @return the first non-null MatchContext returned by <code>matcher.match()</code>, if any;
     * otherwise, null
     */
    // RMF 5/1/2006 - Don't know how to do this easily, given that the visitor
    // implementation essentially traverses the AST in order of increasing offset...
//    public MatchContext findPreviousMatch(Matcher matcher, Object astRoot, int offset);

    /**
     * Returns the file path of the file containing the
     * source text corresponding to the given AST node <code>astNode</code>.
     * The path will be the same as what was provided to the AST creator
     * (e.g. the parser).
     */
    public String getFile(Object astNode);

    /**
     * Returns the character offset (not the byte offset) of the first character of
     * source text corresponding to the given AST node <code>astNode</code>.
     */
    public int getOffset(Object astNode);

    /**
     * Returns the length in characters (not bytes) of the source text
     * corresponding to the given AST node <code>astNode</code>.
     */
    public int getLength(Object astNode);
}
