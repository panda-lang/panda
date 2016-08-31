package org.panda_lang.core.interpreter.token.suggestion;

import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.EqualableToken;

public class Operator extends EqualableToken {

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
        return getType().getTypeName().toLowerCase() + ": " + getToken();
    }

}
