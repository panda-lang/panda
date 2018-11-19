package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.popsuted;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

public class DynamicContent {

    private final int index;
    private final Tokens tokens;

    public DynamicContent(int index, Tokens tokens) {
        this.tokens = tokens;
        this.index = index;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public int getIndex() {
        return index;
    }

}
