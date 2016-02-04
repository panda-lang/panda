package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PLong extends PObject implements Numeric {

    static {
        Vial vial = new Vial("Long");
        vial.group("panda.lang");
    }

    private final long l;

    public PLong(long l) {
        this.l = l;
    }

    @Override
    public Numeric add(Numeric numeric)  {
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

    public long getLong() {
        return l;
    }

    @Override
    public Object getJavaValue() {
        return getLong();
    }

}
