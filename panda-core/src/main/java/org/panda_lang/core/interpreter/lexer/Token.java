package org.panda_lang.core.interpreter.lexer;

public interface Token {

    String getToken();

    default String getName() {
        return getType().getTypeName();
    }

    default TokenType getType() {
        return TokenType.UNKNOWN;
    }

}
