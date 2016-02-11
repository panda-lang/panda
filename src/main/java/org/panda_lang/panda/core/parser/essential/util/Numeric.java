package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.ObjectEssence;

public abstract class Numeric extends ObjectEssence {

    public abstract Number getNumber();

    public abstract NumberType getNumberType();

    public Numeric add(Numeric numeric) {
        return NumericUtils.add(this, numeric);
    }

    public Numeric subtract(Numeric numeric) {
        return null;
    }

    public Numeric multiply(Numeric numeric) {
        return null;
    }

    public Numeric divide(Numeric numeric) {
        return null;
    }

    public byte getByte() {
        return getNumber().byteValue();
    }

    public short getShort() {
        return getNumber().shortValue();
    }

    public int getInt() {
        return getNumber().intValue();
    }

    public long getLong() {
        return getNumber().longValue();
    }

    public float getFloat() {
        return getNumber().floatValue();
    }

    public double getDouble() {
        return getNumber().doubleValue();
    }

    @Override
    public String toString() {
        return getJavaValue().toString();
    }

}
