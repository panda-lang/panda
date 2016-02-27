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

    public static Numeric subtract(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return subtract(b, a, true);
        }
        return subtract(a, b, false);
    }

    private static Numeric subtract(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return subtract(b, a, true);
        }
        if (f) {
            switch (a.getNumberType()) {
                case BYTE:
                    return new ByteEssence((byte) (b.getByte() - a.getByte()));
                case SHORT:
                    return new ShortEssence((short) (b.getShort() - a.getShort()));
                case INT:
                    return new IntEssence(b.getInt() - a.getInt());
                case LONG:
                    return new LongEssence(b.getLong() - a.getLong());
                case FLOAT:
                    return new FloatEssence(b.getFloat() - a.getFloat());
                case DOUBLE:
                    return new DoubleEssence(b.getDouble() - a.getDouble());
                case FAT_PANDA:
                    return null;
            }
        }
        switch (a.getNumberType()) {
            case BYTE:
                return new ByteEssence((byte) (a.getByte() - b.getByte()));
            case SHORT:
                return new ShortEssence((short) (a.getShort() - b.getShort()));
            case INT:
                return new IntEssence(a.getInt() - b.getInt());
            case LONG:
                return new LongEssence(a.getLong() - b.getLong());
            case FLOAT:
                return new FloatEssence(a.getFloat() - b.getFloat());
            case DOUBLE:
                return new DoubleEssence(a.getDouble() - b.getDouble());
            case FAT_PANDA:
                return null;
        }
        return null;
    }

    public static Numeric multiply(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return multiply(b, a);
        }
        switch (a.getNumberType()) {
            case BYTE:
                return new ByteEssence((byte) (a.getByte() * b.getByte()));
            case SHORT:
                return new ShortEssence((short) (a.getShort() * b.getShort()));
            case INT:
                return new IntEssence(a.getInt() * b.getInt());
            case LONG:
                return new LongEssence(a.getLong() * b.getLong());
            case FLOAT:
                return new FloatEssence(a.getFloat() * b.getFloat());
            case DOUBLE:
                return new DoubleEssence(a.getDouble() * b.getDouble());
            case FAT_PANDA:
                break;
        }
        return null;
    }

    public static Numeric divide(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return divide(b, a, true);
        }
        return divide(a, b, false);
    }

    private static Numeric divide(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return subtract(b, a, true);
        }
        if (f) {
            switch (a.getNumberType()) {
                case BYTE:
                    return new ByteEssence((byte) (b.getByte() / a.getByte()));
                case SHORT:
                    return new ShortEssence((short) (b.getShort() / a.getShort()));
                case INT:
                    return new IntEssence(b.getInt() / a.getInt());
                case LONG:
                    return new LongEssence(b.getLong() / a.getLong());
                case FLOAT:
                    return new FloatEssence(b.getFloat() / a.getFloat());
                case DOUBLE:
                    return new DoubleEssence(b.getDouble() / a.getDouble());
                case FAT_PANDA:
                    return null;
            }
        }
        switch (a.getNumberType()) {
            case BYTE:
                return new ByteEssence((byte) (a.getByte() / b.getByte()));
            case SHORT:
                return new ShortEssence((short) (a.getShort() / b.getShort()));
            case INT:
                return new IntEssence(a.getInt() / b.getInt());
            case LONG:
                return new LongEssence(a.getLong() / b.getLong());
            case FLOAT:
                return new FloatEssence(a.getFloat() / b.getFloat());
            case DOUBLE:
                return new DoubleEssence(a.getDouble() / b.getDouble());
            case FAT_PANDA:
                return null;
        }
        return null;
    }

    public static BooleanEssence isGreaterThan(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isGreaterThan(b, a, true);
        }
        return isGreaterThan(a, b, false);
    }

    private static BooleanEssence isGreaterThan(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isGreaterThan(b, a, true);
        }
        boolean flag = false;
        switch (a.getNumberType()) {
            case BYTE:
                flag = f ? b.getByte() > a.getByte() : a.getByte() > b.getByte();
                break;
            case SHORT:
                flag = f ? b.getShort() > a.getShort() : a.getShort() > b.getShort();
                break;
            case INT:
                flag = f ? b.getInt() > a.getInt() : a.getInt() > b.getInt();
                break;
            case LONG:
                flag = f ? b.getLong() > a.getLong() : a.getLong() > b.getLong();
                break;
            case FLOAT:
                flag = f ? b.getFloat() > a.getFloat() : a.getFloat() > b.getFloat();
                break;
            case DOUBLE:
                flag = f ? b.getDouble() > a.getDouble() : a.getDouble() > b.getDouble();
                break;
            case FAT_PANDA:
                break;
        }
        return new BooleanEssence(flag);
    }

    public static BooleanEssence isLessThan(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isLessThan(b, a, true);
        }
        return isLessThan(a, b, false);
    }

    private static BooleanEssence isLessThan(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isLessThan(b, a, true);
        }
        boolean flag = false;
        switch (a.getNumberType()) {
            case BYTE:
                flag = f ? b.getByte() < a.getByte() : a.getByte() < b.getByte();
                break;
            case SHORT:
                flag = f ? b.getShort() < a.getShort() : a.getShort() < b.getShort();
                break;
            case INT:
                flag = f ? b.getInt() < a.getInt() : a.getInt() < b.getInt();
                break;
            case LONG:
                flag = f ? b.getLong() < a.getLong() : a.getLong() < b.getLong();
                break;
            case FLOAT:
                flag = f ? b.getFloat() < a.getFloat() : a.getFloat() < b.getFloat();
                break;
            case DOUBLE:
                flag = f ? b.getDouble() < a.getDouble() : a.getDouble() < b.getDouble();
                break;
            case FAT_PANDA:
                break;
        }
        return new BooleanEssence(flag);
    }

    public static boolean isALessThanB(Numeric a, Numeric b) {
        boolean bFloat = b.getNumberType().isFloatingPoint();
        if (a.getNumberType().isFloatingPoint())
            return bFloat ? a.getDouble() < b.getDouble() : a.getDouble() < b.getLong();
        else if (bFloat)
            return a.getLong() < b.getDouble();
        else
            return a.getLong() < b.getLong();
    }

    public static BooleanEssence isGreaterThanOrEquals(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isGreaterThanOrEquals(b, a, true);
        }
        return isGreaterThanOrEquals(a, b, false);
    }

    private static BooleanEssence isGreaterThanOrEquals(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isGreaterThanOrEquals(b, a, true);
        }
        boolean flag = false;
        switch (a.getNumberType()) {
            case BYTE:
                flag = f ? b.getByte() >= a.getByte() : a.getByte() >= b.getByte();
                break;
            case SHORT:
                flag = f ? b.getShort() >= a.getShort() : a.getShort() >= b.getShort();
                break;
            case INT:
                flag = f ? b.getInt() >= a.getInt() : a.getInt() >= b.getInt();
                break;
            case LONG:
                flag = f ? b.getLong() >= a.getLong() : a.getLong() >= b.getLong();
                break;
            case FLOAT:
                flag = f ? b.getFloat() >= a.getFloat() : a.getFloat() >= b.getFloat();
                break;
            case DOUBLE:
                flag = f ? b.getDouble() >= a.getDouble() : a.getDouble() >= b.getDouble();
                break;
            case FAT_PANDA:
                break;
        }
        return new BooleanEssence(flag);
    }

    public static BooleanEssence isLessThanOrEquals(Numeric a, Numeric b) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isLessThanOrEquals(b, a, true);
        }
        return isLessThanOrEquals(a, b, false);
    }

    private static BooleanEssence isLessThanOrEquals(Numeric a, Numeric b, boolean f) {
        if (a.getNumberType().hasGreaterRangeThan(b)) {
            return isLessThanOrEquals(b, a, true);
        }
        boolean flag = false;
        switch (a.getNumberType()) {
            case BYTE:
                flag = f ? b.getByte() <= a.getByte() : a.getByte() <= b.getByte();
                break;
            case SHORT:
                flag = f ? b.getShort() <= a.getShort() : a.getShort() <= b.getShort();
                break;
            case INT:
                flag = f ? b.getInt() <= a.getInt() : a.getInt() <= b.getInt();
                break;
            case LONG:
                flag = f ? b.getLong() <= a.getLong() : a.getLong() <= b.getLong();
                break;
            case FLOAT:
                flag = f ? b.getFloat() <= a.getFloat() : a.getFloat() <= b.getFloat();
                break;
            case DOUBLE:
                flag = f ? b.getDouble() <= a.getDouble() : a.getDouble() <= b.getDouble();
                break;
            case FAT_PANDA:
                break;
        }
        return new BooleanEssence(flag);
    }

}
