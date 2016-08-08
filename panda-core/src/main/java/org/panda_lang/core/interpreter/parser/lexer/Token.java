package org.panda_lang.core.interpreter.parser.lexer;

public interface Token {

    String getToken();

    default TokenType getType() {
        return TokenType.UNKNOWN;
    }

}
