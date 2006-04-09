package com.ibm.watson.safari.xform.pattern;

public class NodeConstraint {
    private final AttributeAccessor fLhs;
    private final AttributeAccessor fRhs;
    private final Operator fOp;

    public NodeConstraint(AttributeAccessor lhs, Operator op, AttributeAccessor rhs) {
        fLhs= lhs;
        fOp= op;
        fRhs= rhs;
    }

    public AttributeAccessor getLhs() {
        return fLhs;
    }

    public Operator getOp() {
        return fOp;
    }

    public AttributeAccessor getRhs() {
        return fRhs;
    }

    public boolean evaluate() {
        return false; // TODO do something
    }
}
