/*
 * (C) Copyright IBM Corporation 2007
 * 
 * This file is part of the Eclipse IMP.
 */
/**
 * 
 */
package org.eclipse.imp.xform.pattern.matching;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MatchResult {
    final Object fRoot;
    final MatchResult fParent;
    final Map<String, Object> fBindings= new HashMap<String,Object>();

    Object fMatchNode;

    public MatchResult(Object root) {
        this(root, null);
    }

    public MatchResult(Object root, MatchResult parent) {
	fRoot= root;
	fParent= parent;
    }

    public Object getRoot() {
        return fRoot;
    }

    public MatchResult getParent() {
        return fParent;
    }

    /*package*/ void setMatchNode(Object node) {
        fMatchNode= node;
    }

    public Object getMatchNode() {
        return fMatchNode;
    }

    public Map<String, Object> localBindings() {
	return fBindings;
    }

    public Map<String, Object> getBindings() {
	Map<String, Object> allBindings= new HashMap<String,Object>();
	MatchResult r= this;
	while (r != null) {
	    allBindings.putAll(r.fBindings);
	    r= r.fParent;
	}
        return allBindings;
    }

    /*package*/ void addBinding(String varName, Object astNode) {
        fBindings.put(varName, astNode);
    }

    public String toString() {
        StringBuffer buff= new StringBuffer();

        buff.append("node = '");
        buff.append(fMatchNode);
        buff.append("'; bindings = { ");
        for(Iterator<String> iter= fBindings.keySet().iterator(); iter.hasNext(); ) {
            String metaVarName= iter.next();

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
