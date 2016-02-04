package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.PObject;

public abstract class Numeric extends PObject {

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
        return (byte) getNumber();
    }

    public short getShort() {
        return (short) getNumber();
    }

    public int getInt() {
        return (int) getNumber();
    }

    public long getLong() {
        return (long) getNumber();
    }

    public float getFloat() {
        return (float) getNumber();
    }

    public double getDouble() {
        return (double) getNumber();
    }

    public Object getNumber() {
        return getJavaValue();
    }

    public abstract NumberType getNumberType();

}
