package org.panda_lang.panda.core.element;

public class Literal implements Token {

    private final String[] literal;

    public Literal(char literal) {
        this(Character.toString(literal));
    }

    public Literal(String literal) {
        this(new String[]{ literal, literal });
    }

    public Literal(String literalStart, String literalEnd) {
        this(new String[]{ literalStart, literalEnd });
    }

    public Literal(String[] literal) {
        if (literal.length == 0) {
            this.literal = new String[]{ "", "" };
        }
        else if (literal.length == 1) {
            this.literal = new String[]{ literal[0], literal[0] };
        }
        else {
            this.literal = literal;
        }
    }

    @Override
    public String getToken() {
        return getLiteralStart() + getLiteralEnd();
    }

    public String getLiteralStart() {
        return literal[0];
    }

    public String getLiteralEnd() {
        return literal[1];
    }

    public String[] getLiteral() {
        return literal;
    }

}
