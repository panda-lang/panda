package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PShort extends PObject  implements Numeric{

    static {
        Vial vial = new Vial("Short");
        vial.group("panda.lang");
    }

    private final short s;

    public PShort(short s) {
        this.s = s;
    }

    @Override
    public Numeric add(Numeric numeric) {
        return null;
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

    @Override
    public NumberType getNumberType() {
        return null;
    }

    @Override
    public Object getNumber() {
        return null;
    }

    public short getShort() {
        return s;
    }

    @Override
    public Object getJavaValue() {
        return getShort();
    }

}
