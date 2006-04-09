package com.ibm.watson.safari.xform.pattern;

/**
 * @author rfuhrer@watson.ibm.com
 *
 */
public class AttributeLiteral extends AttributeAccessor {
    private Value fValue;

    public AttributeLiteral(Value value) {
        fValue= value;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.safari.xform.pattern.AttributeAccessor#getValue()
     */
    public Value getValue() {
        return fValue;
    }
}
