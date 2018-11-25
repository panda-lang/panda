package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.popsuted;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;

class PopsutedTokenExtractor {

    private final TokenPattern pattern;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = new ArrayList<>();

    public PopsutedTokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public TokenExtractorResult extract(Tokens source) {
        return extract(new PandaSourceStream(source));
    }

    public TokenExtractorResult extract(SourceStream source) {
        TokenExtractorWorker worker = new TokenExtractorWorker(pattern);
        return worker.extract(source);
    }

}
