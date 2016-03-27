package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Vial;

public class DoubleEssence extends Numeric {

    static {
        Vial vial = new Vial("Double");
        vial.group("panda.lang");
    }

    private final double d;

    public DoubleEssence(double d) {
        this.d = d;
    }

    @Override
    public double getDouble() {
        return d;
    }

    @Override
    public Number getNumber() {
        return getDouble();
    }

    @Override
    public NumberType getNumberType() {
        return NumberType.DOUBLE;
    }

    @Override
    public Object getJavaValue() {
        return getDouble();
    }

}
