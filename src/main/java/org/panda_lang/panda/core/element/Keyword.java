package org.panda_lang.panda.core.element;

public class Keyword implements Token {

    private final String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getToken() {
        return keyword;
    }

}
