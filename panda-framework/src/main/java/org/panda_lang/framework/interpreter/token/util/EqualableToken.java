package org.panda_lang.framework.interpreter.token.util;

import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;

public abstract class EqualableToken implements Token {

    @Override
    public abstract String getTokenValue();

    @Override
    public abstract TokenType getType();

    @Override
    public int hashCode() {
        int result = getTokenValue().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EqualableToken)) {
            return false;
        }

        EqualableToken that = (EqualableToken) o;

        return getTokenValue().equals(that.getTokenValue()) && getType().equals(that.getType());

    }

}
