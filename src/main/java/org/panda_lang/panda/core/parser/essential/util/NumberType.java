package org.panda_lang.panda.core.parser.essential.util;

public enum NumberType {

    BYTE("B"),
    SHORT("S"),
    INT("I"),
    LONG("L"),
    FLOAT("F"),
    DOUBLE("D"),
    FAT_PANDA("P");

    private final String unit;

    NumberType(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

}
