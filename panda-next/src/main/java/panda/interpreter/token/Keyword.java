package panda.interpreter.token;

import panda.interpreter.token.Keyword.KeywordType;

public final class Keyword extends Token<KeywordType> {

    public enum KeywordType {

        MODULE,
        TYPE,
        FUN,
        FROM,
        RETURN,
        REQUIRE,
        IMPORT,
        ;

        private static final KeywordType[] VALUES = values();
        private final String name;

        KeywordType() {
            this.name = name().toLowerCase();
        }

        public String getName() {
            return name;
        }

        public static KeywordType[] getValues() {
            return VALUES;
        }

    }

    public Keyword(KeywordType keywordType, int line, int caret) {
        super(TokenType.KEYWORD, keywordType, line, caret);
    }

    @Override
    public String toSourceString() {
        return getValue().name().toLowerCase();
    }

}
