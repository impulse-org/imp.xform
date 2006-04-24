package com.ibm.watson.safari.xform.pattern;

import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

/**
 * @author rfuhrer@watson.ibm.com
 */
public interface AccessorAdapter {
    public static final String TARGET_TYPE= "targetType";

    public Object getValue(NodeAttribute attribute, Object astNode);
    public Object getValue(String attributeName, Object astNode);

    /**
     * @param astNode
     * @return
     */
    public Object[] getChildren(Object astNode);

    public boolean isInstanceOfType(Object astNode, String typeName);
}
