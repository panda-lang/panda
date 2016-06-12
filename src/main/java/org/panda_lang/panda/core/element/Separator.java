package org.panda_lang.panda.core.element;

public class Separator implements Token {

    private final char separator;

    public Separator(char separator) {
        this.separator = separator;
    }

    @Override
    public String getToken() {
        return Character.toString(separator);
    }

    public char getSeparator() {
        return separator;
    }

}
