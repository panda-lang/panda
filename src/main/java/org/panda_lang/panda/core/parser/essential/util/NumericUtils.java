package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.*;

public class NumericUtils {

    public static Numeric add(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return add(b, a);
        }
        switch (a.getNumberType()) {
            case BYTE:
                return new ByteEssence((byte) (a.getByte() + b.getByte()));
            case SHORT:
                return new ShortEssence((short) (a.getShort() + b.getShort()));
            case INT:
                return new IntEssence(a.getInt() + b.getInt());
            case LONG:
                return new LongEssence(a.getLong() + b.getLong());
            case FLOAT:
                return new FloatEssence(a.getFloat() + b.getFloat());
            case DOUBLE:
                return new DoubleEssence(a.getDouble() + b.getDouble());
            case FAT_PANDA:
                break;
        }
        return null;
    }

}
