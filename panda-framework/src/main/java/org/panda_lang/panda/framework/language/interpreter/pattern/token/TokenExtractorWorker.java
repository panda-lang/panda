package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

public class TokenExtractorWorker {

    private final TokenPattern pattern;

    public TokenExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public TokenExtractorResult extract(TokenizedSource source) {
        return new TokenExtractorResult();
    }

}
