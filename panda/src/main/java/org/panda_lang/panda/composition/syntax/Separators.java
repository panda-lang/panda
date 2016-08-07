package org.panda_lang.panda.composition.syntax;

public class Separators {

    public static final String SEMICOLON = ";";

    public static final String COMMA = ",";

    public static final String PERIOD = ".";

    public static final String LEFT_PARENTHESIS_DELIMITER = "(";

    public static final String RIGHT_PARENTHESIS_DELIMITER = ")";

    public static final String LEFT_BRACE_DELIMITER = "{";

    public static final String RIGHT_BRACE_DELIMITER = "}";

    public static final String LEFT_BRACKET_DELIMITER = "[";

    public static final String RIGHT_BRACKET_DELIMITER = "]";

    private static final String[] VALUES = new String[9];

    static {
        VALUES[0] = SEMICOLON;
        VALUES[1] = COMMA;
        VALUES[2] = PERIOD;

        VALUES[3] = LEFT_PARENTHESIS_DELIMITER;
        VALUES[4] = RIGHT_PARENTHESIS_DELIMITER;

        VALUES[5] = LEFT_BRACE_DELIMITER;
        VALUES[6] = RIGHT_BRACE_DELIMITER;

        VALUES[7] = LEFT_BRACKET_DELIMITER;
        VALUES[8] = RIGHT_BRACKET_DELIMITER;
    }

    public static String[] values() {
        return VALUES;
    }

}
