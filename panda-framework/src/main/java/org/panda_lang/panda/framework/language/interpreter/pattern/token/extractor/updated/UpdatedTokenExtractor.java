package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;

public class UpdatedTokenExtractor {

    private final TokenPattern pattern;

    public UpdatedTokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public ExtractorResult extract(SourceStream source) {
        return new ExtractorWorker(pattern).extract(source);
    }

}
