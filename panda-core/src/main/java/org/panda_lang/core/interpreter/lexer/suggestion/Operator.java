package org.panda_lang.core.interpreter.lexer.suggestion;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenType;

public class Operator implements Token {

    private final String operator;

    public Operator(String operator) {
        this.operator = operator;
    }

    @Override
    public String getToken() {
        return operator;
    }

    @Override
    public TokenType getType() {
        return TokenType.OPERATOR;
    }

    @Override
    public String toString() {
        return getType().name().toLowerCase() + ": " + getToken();
    }

}
