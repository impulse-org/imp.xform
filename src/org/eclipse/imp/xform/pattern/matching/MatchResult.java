/**
 * 
 */
package com.ibm.watson.safari.xform.pattern.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MatchResult {
    Object fRoot;
    Object fMatchNode;
    Map/*<String, Object astNode>*/ fBindings= new HashMap();

    public MatchResult(Object root) {
        fRoot= root;
    }
    public Object getRoot() {
        return fRoot;
    }
    /*package*/ void setMatchNode(Object node) {
        fMatchNode= node;
    }
    public Object getMatchNode() {
        return fMatchNode;
    }
    public Map/*<String, Object astNode>*/ getBindings() {
        return fBindings;
    }
    /*package*/ void addBinding(String varName, Object astNode) {
        fBindings.put(varName, astNode);
    }
    public String toString() {
        StringBuffer buff= new StringBuffer();

        buff.append("node = '");
        buff.append(fMatchNode);
        buff.append("'; bindings = { ");
        for(Iterator iter= fBindings.keySet().iterator(); iter.hasNext(); ) {
            String metaVarName= (String) iter.next();

            buff.append(metaVarName);
            buff.append(" => '");
            buff.append(fBindings.get(metaVarName));
            buff.append("'");
            if (iter.hasNext()) buff.append(", ");
        }
        buff.append(" }");
        return buff.toString();
    }
}