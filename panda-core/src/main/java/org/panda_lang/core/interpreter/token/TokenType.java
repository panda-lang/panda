package org.panda_lang.core.interpreter.token;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenType {

    private static final AtomicInteger ID_ASSIGNER = new AtomicInteger();

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

    private final int id;
    private final String typeName;

    public TokenType(String typeName) {
        this.id = ID_ASSIGNER.getAndIncrement();
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int ordinal() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TokenType)) {
            return false;
        }

        TokenType tokenType = (TokenType) o;

        return id == tokenType.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public static TokenType[] values() {
        return VALUES;
    }

}
