package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractorResult;

public class TokenPattern {

    private final LexicalPatternElement patternContent;

    TokenPattern(TokenPatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    public TokenExtractorResult extract(TokenizedSource source) {
        TokenExtractor extractor = new TokenExtractor(this);
        return extractor.extract(source);
    }

    public LexicalPatternElement getPatternContent() {
        return patternContent;
    }

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

}
