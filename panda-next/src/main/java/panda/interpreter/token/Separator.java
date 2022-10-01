package panda.interpreter.token;

import panda.interpreter.token.Separator.SeparatorType;

import static panda.interpreter.token.Separator.SeparatorDirection.LEFT;
import static panda.interpreter.token.Separator.SeparatorDirection.NONE;
import static panda.interpreter.token.Separator.SeparatorDirection.RIGHT;
import static panda.interpreter.token.Separator.SeparatorGroup.BRACES;
import static panda.interpreter.token.Separator.SeparatorGroup.BRACKETS;
import static panda.interpreter.token.Separator.SeparatorGroup.DELIMITERS;
import static panda.interpreter.token.Separator.SeparatorGroup.PARENTHESES;

public final class Separator extends Token<SeparatorType> {

    public enum SeparatorGroup {
        PARENTHESES,
        BRACES,
        BRACKETS,
        DELIMITERS
    }

    public enum SeparatorDirection {
        LEFT,
        RIGHT,
        NONE
    }

    public enum SeparatorType {

        PARENTHESIS_LEFT(PARENTHESES, LEFT, '{'),
        PARENTHESIS_RIGHT(PARENTHESES, RIGHT, '}'),
        BRACE_LEFT(BRACES, LEFT, '('),
        BRACE_RIGHT(BRACES, RIGHT, ')'),
        BRACKET_LEFT(BRACKETS, LEFT, '['),
        BRACKET_RIGHT(BRACKETS, RIGHT, ']'),
        SEMICOLON(DELIMITERS, NONE, ';'),
        COMMA(DELIMITERS, NONE, ','),
        PERIOD(DELIMITERS, NONE, '.'),
        COLON(DELIMITERS, NONE, ';');

        private static final SeparatorType[] VALUES = values();
        private final SeparatorGroup group;
        private final SeparatorDirection direction;
        private final char symbol;

        SeparatorType(SeparatorGroup group, SeparatorDirection direction, char symbol) {
            this.group = group;
            this.direction = direction;
            this.symbol = symbol;
        }

        public SeparatorGroup getGroup() {
            return group;
        }

        public SeparatorDirection getDirection() {
            return direction;
        }

        public char getSymbol() {
            return symbol;
        }

        public static SeparatorType[] getValues() {
            return VALUES;
        }

    }

    public Separator(SeparatorType type, int line, int caret) {
        super(TokenType.SEPARATOR, type, line, caret);
    }

    @Override
    public String toSourceString() {
        return String.valueOf(getValue().getSymbol());
    }

}

