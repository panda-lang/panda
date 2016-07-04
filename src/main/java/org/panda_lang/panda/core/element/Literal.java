package org.panda_lang.panda.core.element;

public class Literal implements Token {

    private final String name;
    private final String pattern;

    public Literal(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    @Override
    public String getToken() {
        return getPattern();
    }

    public String getPattern() {
        return pattern;
    }

    public String getName() {
        return name;
    }

}
