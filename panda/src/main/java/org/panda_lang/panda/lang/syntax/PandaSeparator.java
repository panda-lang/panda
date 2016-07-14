package org.panda_lang.panda.lang.syntax;

public class PandaSeparator {

    public static final String BLOCK_START = "{";

    public static final String BLOCK_END = "}";

    public static final String END_LINE = ";";

    public static final String SEPARATOR = ",";

    private static final String[] VALUES = new String[] { BLOCK_START, BLOCK_END, END_LINE, SEPARATOR };

    public static String[] values() {
        return VALUES;
    }

}
