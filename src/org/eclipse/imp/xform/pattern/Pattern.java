package com.ibm.watson.safari.xform.pattern;

import java.util.Collections;
import java.util.Set;

/**
 * @author rfuhrer@watson.ibm.com
 *
 */
public class Pattern {
    private Node fRootNode;

    public Pattern(Node rootNode) {
        fRootNode= rootNode;
    }

    public Node getRootNode() {
        return fRootNode;
    }

    public Set/*<Binding>*/ getBindings() {
        return Collections.EMPTY_SET;
    }
}
