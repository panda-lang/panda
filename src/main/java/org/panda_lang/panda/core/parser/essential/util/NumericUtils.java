package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.*;

public class NumericUtils
{

    public static Numeric add(Numeric a, Numeric b)
    {
        if (a.getNumberType().hasGreaterRangeThan(b))
        {
            return add(b, a);
        }
        switch (a.getNumberType())
        {
            case BYTE:
                return new PByte((byte) (a.getByte() + b.getByte()));
            case SHORT:
                return new PShort((short) (a.getShort() + b.getShort()));
            case INT:
                return new PInt(a.getInt() + b.getInt());
            case LONG:
                return new PLong(a.getLong() + b.getLong());
            case FLOAT:
                return new PFloat(a.getFloat() + b.getFloat());
            case DOUBLE:
                return new PDouble(a.getDouble() + b.getDouble());
            case FAT_PANDA:
                break;
        }
        return null;
    }

}
