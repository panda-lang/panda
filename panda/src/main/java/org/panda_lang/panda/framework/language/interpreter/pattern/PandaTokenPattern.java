package org.panda_lang.panda.framework.language.interpreter.pattern;

import org.panda_lang.panda.framework.language.interpreter.pattern.readers.PandaExpressionReaders;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPatternBuilder;

public class PandaTokenPattern {

    private final TokenPatternBuilder builder = TokenPattern.builder();

    private PandaTokenPattern() { }

    public PandaTokenPattern compile(String pattern) {
        builder.compile(pattern);
        return this;
    }

    public TokenPattern build() {
        TokenPattern pattern = TokenPattern.builder()
                .build();

        return pattern.addWildcardReaders(PandaExpressionReaders.getDefaults());
    }

    public static PandaTokenPattern builder() {
        return new PandaTokenPattern();
    }

}
