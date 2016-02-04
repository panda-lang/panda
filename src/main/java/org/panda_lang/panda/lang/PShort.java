package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Vial;

public class PShort extends Numeric {

    static {
        Vial vial = new Vial("Short");
        vial.group("panda.lang");
    }

    private final short s;

    public PShort(short s) {
        this.s = s;
    }

    @Override
    public NumberType getNumberType() {
        return NumberType.SHORT;
    }

    @Override
    public short getShort() {
        return s;
    }

    @Override
    public Object getJavaValue() {
        return getShort();
    }

}
