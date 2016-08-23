package org.panda_lang.panda.composition.syntax;

import org.panda_lang.core.interpreter.lexer.suggestion.Separator;

public class Separators {

    public static final Separator SEMICOLON = new Separator(";");

    public static final Separator COMMA = new Separator(",");

    public static final Separator PERIOD = new Separator(".");

    public static final Separator LEFT_PARENTHESIS_DELIMITER = new Separator("(");

    public static final Separator RIGHT_PARENTHESIS_DELIMITER = new Separator(")");

    public static final Separator LEFT_BRACE_DELIMITER = new Separator("{");

    public static final Separator RIGHT_BRACE_DELIMITER = new Separator("}");

    public static final Separator LEFT_BRACKET_DELIMITER = new Separator("[");

    public static final Separator RIGHT_BRACKET_DELIMITER = new Separator("]");

    private static final Separator[] VALUES = new Separator[9];

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

        LEFT_BRACE_DELIMITER.setOpposite(RIGHT_BRACE_DELIMITER);
        LEFT_BRACKET_DELIMITER.setOpposite(RIGHT_BRACKET_DELIMITER);
        LEFT_PARENTHESIS_DELIMITER.setOpposite(RIGHT_PARENTHESIS_DELIMITER);
    }

    public static Separator valueOf(String str) {
        for (Separator separator : values()) {
            if (separator.getToken().equals(str)) {
                return separator;
            }
        }

        return null;
    }

    public static Separator[] values() {
        return VALUES;
    }

}

