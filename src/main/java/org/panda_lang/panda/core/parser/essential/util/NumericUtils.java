package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.lang.PInteger;
import org.panda_lang.panda.lang.PLong;

public class NumericUtils {

    public static Numeric add(Numeric a, Numeric b, boolean invert) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return add(b, a, true);
        }
        switch (a.getNumberType()) {
            case BYTE:
                break;
            case SHORT:
                break;
            case INT:
                break;
            case LONG:
                break;
            case FLOAT:
                break;
            case DOUBLE:
                break;
            case FAT_PANDA:
                break;
        }
        return null;
    }

    public static Numeric add(PInteger pa, Numeric pb, boolean invert) {
        if (pb.getNumberType().hasGreaterRangeThan(pa)) {
            return add(pb, pa, true);
        }
        int a = pa.getInteger();
        int b = (int) pb.getNumber();
        return invert ? new PInteger(b + a) : new PInteger(a + b);
    }

    public static Numeric add(PLong pa, Numeric pb, boolean invert) {
        if (pb.getNumberType().hasGreaterRangeThan(pa)) {
            return add(pb, pa, true);
        }
        long a = pa.getLong();
        long b = (long) pb.getNumber();
        return invert ? new PLong(b + a) : new PLong(a + b);
    }

}
