package org.panda_lang.panda.lang.syntax;

import org.panda_lang.panda.core.element.Literal;

public enum PandaLiteral {

    DOUBLE_QUOTES('"'),
    SINGLE_QUOTES('\'');

    private final Literal literal;

    PandaLiteral(char literal) {
        this(Character.toString(literal));
    }

    PandaLiteral(String literal) {
        this(literal, literal);
    }

    PandaLiteral(String literalStart, String literalEnd) {
        this(new String[]{ literalStart, literalEnd });
    }

    PandaLiteral(String[] literal) {
        this.literal = new Literal(literal);
    }

    public Literal getLiteral() {
        return literal;
    }

}
