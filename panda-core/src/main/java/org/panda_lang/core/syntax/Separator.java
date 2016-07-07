package org.panda_lang.core.syntax;

public class Separator implements Token {

    private final String separator;

    public Separator(char separator) {
        this(Character.toString(separator));
    }

    public Separator(String separator) {
        this.separator = separator;
    }

    @Override
    public String getToken() {
        return separator;
    }

}
