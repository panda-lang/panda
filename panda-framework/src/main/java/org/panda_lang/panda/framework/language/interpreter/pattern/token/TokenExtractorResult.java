package org.panda_lang.panda.framework.language.interpreter.pattern.token;

public class TokenExtractorResult {

    private final boolean matched;

    public TokenExtractorResult() {
        this.matched = false;
    }

    public boolean isMatched() {
        return matched;
    }

}
