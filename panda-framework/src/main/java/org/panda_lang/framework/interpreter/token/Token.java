package org.panda_lang.framework.interpreter.token;

public interface Token {

    String getTokenValue();

    default String getName() {
        return getType().getTypeName();
    }

    default TokenType getType() {
        return TokenType.UNKNOWN;
    }

}
