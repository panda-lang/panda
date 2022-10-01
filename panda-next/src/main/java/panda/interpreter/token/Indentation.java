package panda.interpreter.token;

import panda.utilities.StringUtils;

public final class Indentation extends Token<Integer> {

    public Indentation(Integer level, int line, int caret) {
        super(TokenType.INDENTATION, level, line, caret);
    }

    @Override
    public String toSourceString() {
        return StringUtils.buildSpace(getValue() * 2);
    }

}
