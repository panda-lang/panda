package org.panda_lang.panda.composition.syntax;

public class Sequences {

    public static final String STRING = "\"";

    public static final String CHARACTER = "'";

    private static final String[] VALUES = new String[2];

    static {
        VALUES[0] = STRING;
        VALUES[1] = CHARACTER;
    }

    public static String[] values() {
        return VALUES;
    }

}
