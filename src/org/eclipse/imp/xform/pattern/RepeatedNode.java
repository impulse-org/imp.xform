package com.ibm.watson.safari.xform.pattern;

public class RepeatedNode extends Node {
    private IntegerBound fLowerBound;

    private IntegerBound fUpperBound;

    public RepeatedNode() {
        super();
    }

    public IntegerBound getLowerBound() {
        return fLowerBound;
    }

    public void setLowerBound(IntegerBound lowerBound) {
        fLowerBound= lowerBound;
    }

    public IntegerBound getUpperBound() {
        return fUpperBound;
    }

    public void setUpperBound(IntegerBound upperBound) {
        fUpperBound= upperBound;
    }
}
