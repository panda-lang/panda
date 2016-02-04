package org.panda_lang.panda.core.parser.essential.util;

public class NumericUtils {

    public static Numeric add(Numeric a, Numeric b) {
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

}
