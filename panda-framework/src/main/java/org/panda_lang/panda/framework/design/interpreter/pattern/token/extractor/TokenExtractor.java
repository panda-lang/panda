package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;

public class TokenExtractor {

    private final TokenPattern pattern;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public ExtractorResult extract(SourceStream source) {
        return new ExtractorWorker(pattern).extract(source);
    }

}
