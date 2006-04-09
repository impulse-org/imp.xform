package com.ibm.watson.safari.xform.pattern;

/**
 * @author rfuhrer@watson.ibm.com
 *
 */
public class StringValue implements Value {
    private String fValue;

    public StringValue(String value) {
        fValue= value;
    }

    public String getValue() {
        return fValue;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof StringValue)) return false;

        StringValue other= (StringValue) o;

        return (other.fValue == fValue);
    }

    public int hashCode() {
        return 131 + 439 * fValue.hashCode();
    }
}
