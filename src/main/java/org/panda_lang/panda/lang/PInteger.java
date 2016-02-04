package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PInteger extends PObject implements Numeric {

    static {
        Vial vial = new Vial("Integer");
        vial.group("panda.lang");
    }

    private final int i;

    public PInteger(int i) {
        this.i = i;
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

    public int getInteger() {
        return i;
    }

    @Override
    public Object getJavaValue() {
        return getInteger();
    }

}
