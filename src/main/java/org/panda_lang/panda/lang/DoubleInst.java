package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Structure;

public class DoubleInst extends Numeric {

    static {
        Structure structure = new Structure("Double");
        structure.group("panda.lang");
    }

    private final double d;

    public DoubleInst(double d) {
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
