package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;

public class TokenExtractor {

    private final TokenPattern pattern;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public TokenExtractorResult extract(TokenizedSource source) {
        TokenExtractorWorker worker = new TokenExtractorWorker(pattern);
        return worker.extract(source);
    }

}
