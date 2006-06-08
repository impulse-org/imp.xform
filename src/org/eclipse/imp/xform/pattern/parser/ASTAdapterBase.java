/*
 * Created on Jun 6, 2006
 */
package com.ibm.watson.safari.xform.pattern.parser;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.ibm.watson.safari.xform.pattern.matching.IASTAdapter;
import com.ibm.watson.safari.xform.pattern.matching.MatchResult;
import com.ibm.watson.safari.xform.pattern.matching.Matcher;
import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

public abstract class ASTAdapterBase implements IASTAdapter {
    private static Object[] EMPTY= new Object[0];

    public Object getValue(NodeAttribute attribute, Object astNode) { return null; }

    public Object getValue(String attributeName, Object astNode) { return null; }

    public Object[] getChildren(Object astNode) { return EMPTY; }

    public boolean isInstanceOfType(Object astNode, String typeName) {return false; }

    public Set findAllMatches(Matcher matcher, Object astRoot) {
        return Collections.EMPTY_SET;
    }

    public MatchResult findNextMatch(Matcher matcher, Object astRoot, int offset) {
        return null;
    }

    public String getFile(Object astNode) {
	return null;
    }

    public int getOffset(Object astNode) {
	return 0;
    }

    public int getLength(Object astNode) {
	return 0;
    }

    public String lookupSimpleNodeType(String simpleName) {
        return null;
    }

    public boolean isSubTypeOf(String maybeSuper, String maybeSub) {
	try {
	    return Class.forName(maybeSuper).isAssignableFrom(Class.forName(maybeSuper));
	} catch (ClassNotFoundException e) {
	    assert(false): e.getMessage();
	    return false;
	}
    }

    public String getTypeOf(Object astNode) {
        return null;
    }

    public Object construct(String qualName, Object[] children) throws IllegalArgumentException {
        return null;
    }

    public Object construct(String qualName, Object[] children, Map attribs) throws IllegalArgumentException {
        return null;
    }
}