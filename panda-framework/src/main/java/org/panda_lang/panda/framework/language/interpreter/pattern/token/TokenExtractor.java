package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;

class TokenExtractor {

    private final TokenPattern pattern;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    protected TokenExtractorResult extract(TokenizedSource source) {
        TokenExtractorWorker worker = new TokenExtractorWorker(pattern);
        return worker.extract(source);
    }

}
