package org.panda_lang.core.interpreter.token;

public interface Token {

    String getToken();

    default String getName() {
        return getType().getTypeName();
    }

    default TokenType getType() {
        return TokenType.UNKNOWN;
    }

}
