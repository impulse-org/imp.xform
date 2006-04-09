package com.ibm.watson.safari.xform.pattern;

public class IntegerBound implements Value {
    private int fValue;

    private boolean fInfinity;

    public IntegerBound(int value) {
        fValue= value;
        fInfinity= false;
    }

    public boolean isInfinity() {
        return fInfinity;
    }

    public int getValue() {
        return fValue;
    }

    public void setValue(int value) {
        fValue= value;
    }
}
