package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Structure;

public class LongInst extends Numeric {

    static {
        Structure structure = new Structure("Long");
        structure.group("panda.lang");
    }

    private final long l;

    public LongInst(long l) {
        this.l = l;
    }

    @Override
    public long getLong() {
        return l;
    }

    @Override
    public Number getNumber() {
        return getLong();
    }

    @Override
    public NumberType getNumberType() {
        return NumberType.LONG;
    }

    @Override
    public Object getJavaValue() {
        return getLong();
    }

}
