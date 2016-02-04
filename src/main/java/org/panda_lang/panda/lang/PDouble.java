package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PDouble extends Numeric {

    static {
        Vial vial = new Vial("Double");
        vial.group("panda.lang");
    }

    private final double d;

    public PDouble(double d) {
        this.d = d;
    }

    @Override
    public NumberType getNumberType() {
        return NumberType.DOUBLE;
    }

    @Override
    public double getDouble() {
        return d;
    }

    @Override
    public Object getJavaValue() {
        return getDouble();
    }

}
