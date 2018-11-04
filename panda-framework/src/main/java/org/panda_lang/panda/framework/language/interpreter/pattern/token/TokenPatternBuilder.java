package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.PandaFrameworkException;

public class TokenPatternBuilder {

    protected TokenPatternElement patternContent;

    TokenPatternBuilder() { }

    public TokenPatternBuilder compile(String pattern) {
        TokenPatternCompiler compiler = new TokenPatternCompiler();
        this.patternContent = compiler.compile(pattern);
        return this;
    }

    public TokenPattern build() {
        if (patternContent == null) {
            throw new PandaFrameworkException("Pattern is not defined");
        }

        return new TokenPattern(this);
    }

}
