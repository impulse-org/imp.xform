package com.ibm.watson.safari.xform.pattern;

import com.ibm.watson.safari.xform.pattern.parser.Ast.NodeAttribute;

/**
 * @author rfuhrer@watson.ibm.com
 */
public interface AccessorAdapter {
    public Object getValue(NodeAttribute attribute);
}
