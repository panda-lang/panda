package org.panda_lang.core.interpreter.token.suggestion;

import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.EqualableToken;

public class Separator extends EqualableToken {

    private final String separator;
    private Separator opposite;

    public Separator(char separator) {
        this(Character.toString(separator));
    }

    public Separator(String separator) {
        this.separator = separator;
    }

    public boolean hasOpposite() {
        return opposite != null;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getToken();
    }

    public void setOpposite(Separator opposite) {
        this.opposite = opposite;
    }

    public Separator getOpposite() {
        return opposite;
    }

    @Override
    public String getToken() {
        return separator;
    }

    @Override
    public final TokenType getType() {
        return TokenType.SEPARATOR;
    }

}
