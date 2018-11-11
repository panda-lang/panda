package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.LexicalPatternCompiler;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;

public class TokenPatternBuilder {

    protected LexicalPatternElement patternContent;

    TokenPatternBuilder() { }

    public TokenPatternBuilder compile(String pattern) {
        LexicalPatternCompiler compiler = new LexicalPatternCompiler();
        compiler.enableSplittingByWhitespaces();

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
