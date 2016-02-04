package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PByte extends PObject implements Numeric {

    static {
        Vial vial = new Vial("Byte");
        vial.group("panda.lang");
    }

    private final byte b;

    public PByte(byte b) {
        this.b = b;
    }

    @Override
    public Numeric add(Numeric numeric) {
        return new PByte((byte) (b + (byte) numeric.getNumber()));
    }

    @Override
    public Numeric subtract(Numeric numeric) {
        return null;
    }

    @Override
    public Numeric multiply(Numeric numeric) {
        return null;
    }

    @Override
    public Numeric divide(Numeric numeric) {
        return null;
    }

    public byte getByte() {
        return b;
    }

    @Override
    public NumberType getNumberType() {
        return NumberType.BYTE;
    }

    @Override
    public Object getNumber() {
        return getByte();
    }

    @Override
    public Object getJavaValue() {
        return getByte();
    }

}
