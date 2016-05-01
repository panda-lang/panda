package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.ObjectInst;

public abstract class Numeric extends ObjectInst {

    public abstract Number getNumber();

    public abstract NumberType getNumberType();

    public Numeric add(Numeric numeric) {
        return NumericUtils.add(this, numeric);
    }

    public Numeric subtract(Numeric numeric) {
        return NumericUtils.subtract(this, numeric);
    }

    public Numeric multiply(Numeric numeric) {
        return NumericUtils.multiply(this, numeric);
    }

    public Numeric divide(Numeric numeric) {
        return NumericUtils.divide(this, numeric);
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
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Numeric) {
                if (getNumber().equals(((Numeric) obj).getNumber())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getJavaValue().toString();
    }

}
