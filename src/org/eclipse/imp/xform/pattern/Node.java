package com.ibm.watson.safari.xform.pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    /**
     * Optional meta-variable name
     */
    private String fName;

    private Set/*<NodeConstraint>*/ fConstraints;

    private List/*<Node>*/ fChildren;

    public Node() {
    }

    public String getName() {
        return fName;
    }

    public void setName(String name) {
        fName= name;
    }

    public void addConstraint(NodeConstraint c) {
        if (fConstraints == null)
            fConstraints= new HashSet();

        fConstraints.add(c);
    }

    public Set/*<Node>*/ getConstraints() {
        if (fConstraints == null)
            return Collections.EMPTY_SET;
        return fConstraints;
    }

    public void addChild(Node child, int linkType) {
        if (fChildren == null)
            fChildren= new ArrayList();
        fChildren.add(child);
    }

    public List/*<Node>*/ getChildren() {
        if (fChildren == null)
            return Collections.EMPTY_LIST;
        return fChildren;
    }
}
