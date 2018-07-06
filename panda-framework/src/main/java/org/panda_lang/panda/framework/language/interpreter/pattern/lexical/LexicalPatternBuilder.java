package org.panda_lang.panda.framework.language.interpreter.pattern.lexical;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.*;

public class LexicalPatternBuilder {

    private LexicalPatternElement pattern;

    public LexicalPatternBuilder compile(String pattern) {
        LexicalPatternCompiler compiler = new LexicalPatternCompiler();
        LexicalPatternElement compiledPattern = compiler.compile(pattern);

        if (compiledPattern == null) {
            throw new RuntimeException("Cannot compile pattern: " + pattern);
        }

        this.pattern = compiledPattern;
        return this;
    }

    public LexicalPattern build() {
        return new LexicalPattern(pattern);
    }

}
