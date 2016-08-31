package org.panda_lang.core.interpreter.token.util;

import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;

public abstract class EqualableToken implements Token {

    @Override
    public abstract String getToken();

    @Override
    public abstract TokenType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EqualableToken)) {
            return false;
        }

        EqualableToken that = (EqualableToken) o;

        return getToken().equals(that.getToken()) && getType().equals(that.getType());

    }

    @Override
    public int hashCode() {
        int result = getToken().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }

}
