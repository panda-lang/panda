package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition.defaults.DefaultWildcardConditionFactories;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Collection;

public class TokenPattern {

    private final LexicalPatternElement patternContent;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = DefaultWildcardConditionFactories.getDefaultFactories();

    TokenPattern(TokenPatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    public ExtractorResult extract(Tokens source) {
        return extractor().extract(new PandaSourceStream(source));
    }

    public ExtractorResult extract(SourceStream source) {
        return extractor().extract(source);
    }

    public TokenExtractor extractor() {
        return new TokenExtractor(this);
    }

    public TokenPattern addWildcardConditionFactory(WildcardConditionFactory factory) {
        wildcardConditionFactories.add(factory);
        return this;
    }

    public Collection<? extends WildcardConditionFactory> getWildcardConditionFactories() {
        return wildcardConditionFactories;
    }

    public LexicalPatternElement getPatternContent() {
        return patternContent;
    }

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

}
