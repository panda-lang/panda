package panda.interpreter.token;

import panda.interpreter.token.Literal.LiteralValue;

import static panda.interpreter.token.Literal.LiteralGroup.BOOLEANS;
import static panda.interpreter.token.Literal.LiteralGroup.NUMBERS;
import static panda.interpreter.token.Literal.LiteralGroup.STRINGS;

public final class Literal extends Token<LiteralValue> {

    public enum LiteralGroup {
        STRINGS,
        NUMBERS,
        BOOLEANS
    }

    public enum LiteralType {
        RAW_STRING(STRINGS),
        STRING(STRINGS),
        QUOTED_STRING(STRINGS),
        DECIMAL(NUMBERS),
        HEX(NUMBERS),
        TRUE(BOOLEANS),
        FALSE(BOOLEANS);

        private final LiteralGroup group;

        LiteralType(LiteralGroup group) {
            this.group = group;
        }

        public LiteralGroup getGroup() {
            return group;
        }

    }

    public record LiteralValue(LiteralType type, String literal) {
    }

    public Literal(LiteralValue value, int line, int caret) {
        super(TokenType.LITERAL, value, line, caret);
    }

    @Override
    public String toSourceString() {
        return switch (getValue().type) {
            case RAW_STRING -> '`' + getValue().literal + '`';
            case STRING -> '\'' + getValue().literal + '\'';
            case QUOTED_STRING -> '"' + getValue().literal + '"';
            case DECIMAL, HEX -> getValue().literal;
            case TRUE -> "true";
            case FALSE -> "false";
        };
    }

}
