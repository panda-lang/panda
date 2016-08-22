package org.panda_lang.core.interpreter.lexer;

public class TokenType {

    public static final TokenType IDENTIFIER = new TokenType("IDENTIFIER");

    public static final TokenType KEYWORD = new TokenType("KEYWORD");

    public static final TokenType SEPARATOR = new TokenType("SEPARATOR");

    public static final TokenType SEQUENCE = new TokenType("SEQUENCE");

    public static final TokenType OPERATOR = new TokenType("OPERATOR");

    public static final TokenType UNKNOWN = new TokenType("UNKNOWN");

    private static final TokenType[] VALUES = new TokenType[6];

    static {
        VALUES[0] = IDENTIFIER;
        VALUES[1] = KEYWORD;
        VALUES[2] = SEPARATOR;
        VALUES[3] = SEQUENCE;
        VALUES[4] = OPERATOR;
        VALUES[5] = UNKNOWN;
    }

    private final String typeName;

    public TokenType(String typeName) {
        this.typeName = typeName;
    }

    public String name() {
        return typeName;
    }

    public static TokenType[] values() {
        return VALUES;
    }

}
