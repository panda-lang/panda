package org.panda_lang.panda.framework.language.interpreter.pattern.lexical;

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.WildcardProcessor;

import java.util.ArrayList;
import java.util.Collection;

public class LexicalPatternBuilder<T> {

    private LexicalPatternElement pattern;
    private Collection<WildcardProcessor<T>> wildcardProcessors = new ArrayList<>();

    public LexicalPatternBuilder<T> compile(String pattern) {
        LexicalPatternCompiler compiler = new LexicalPatternCompiler();
        LexicalPatternElement compiledPattern = compiler.compile(pattern);

        if (compiledPattern == null) {
            throw new RuntimeException("Cannot compile pattern: " + pattern);
        }

        this.pattern = compiledPattern;
        return this;
    }

    public LexicalPatternBuilder<T> addWildcardProcessor(WildcardProcessor<T> wildcardProcessor) {
        wildcardProcessors.add(wildcardProcessor);
        return this;
    }

    public LexicalPatternBuilder<T> addWildcardProcessors(Collection<WildcardProcessor<T>> processors) {
        wildcardProcessors.addAll(processors);
        return this;
    }

    public LexicalPattern<T> build() {
        return new LexicalPattern<>(pattern, wildcardProcessors);
    }

}
