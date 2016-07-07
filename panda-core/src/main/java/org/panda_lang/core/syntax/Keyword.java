package org.panda_lang.core.syntax;

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
