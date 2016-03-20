package org.panda_lang.panda.core.parser.essential.util;

public enum NumberType {

    BYTE(0, 'B'),
    SHORT(1, 'S'),
    INT(2, 'I'),
    LONG(3, 'L'),
    FLOAT(4, 'F'),
    DOUBLE(5, 'D'),
    FAT_PANDA(6, 'P');

    private final int id;
    private final char unit;

    NumberType(int id, char unit) {
        this.id = id;
        this.unit = unit;
    }

    public static boolean isUnit(char c) {
        for (NumberType numberType : values()) {
            if (numberType.getUnit() == c) {
                return true;
            }
        }
        return false;
    }

    public static NumberType valueOf(char unit) {
        for (NumberType numberType : values()) {
            if (numberType.getUnit() == unit) {
                return numberType;
            }
        }
        return null;
    }

    public char getUnit() {
        return unit;
    }

    public int getID() {
        return id;
    }

    public boolean isFloatingPoint() {
        return id == 4 || id == 5;
    }

    public boolean hasGreaterRangeThan(Numeric numeric) {
        return hasGreaterRangeThan(numeric.getNumberType());
    }

    public boolean hasGreaterRangeThan(NumberType numberType) {
        return getID() < numberType.getID();
    }

}
