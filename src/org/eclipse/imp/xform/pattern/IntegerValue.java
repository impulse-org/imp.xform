package com.ibm.watson.safari.xform.pattern;

/**
 * @author rfuhrer@watson.ibm.com
 *
 */
public class IntegerValue implements Value {
    private final int fValue;

    public IntegerValue(int value) {
        fValue= value;
    }

    public int getValue() {
        return fValue;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IntegerValue)) return false;

        IntegerValue other= (IntegerValue) o;

        return (other.fValue == fValue);
    }

    public int hashCode() {
        return 197 + 101 * fValue;
    }
}
