package org.panda_lang.core.interpreter.lexer;

public interface Token {

    String getToken();

    default TokenType getType() {
        return TokenType.UNKNOWN;
    }

}
