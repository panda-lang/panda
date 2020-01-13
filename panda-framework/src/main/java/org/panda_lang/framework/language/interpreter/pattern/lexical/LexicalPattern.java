/*
 * Copyright (c) 2015-2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.framework.language.interpreter.pattern.lexical;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractor;
import org.panda_lang.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorResult;
import org.panda_lang.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorWorker;
import org.panda_lang.framework.language.interpreter.pattern.lexical.processed.WildcardProcessor;

/**
 * Powerful pattern based on simple lexical description. Supports:
 *
 * <ul>
 *     <li>optionals: <code>[tokenA]</code></li>
 *     <li>variants: <code>(tokenA|tokenB|tokenC)</code></li>
 *     <li>dynamics: <code>{resolverName}</code></li>
 *     <li>wildcards with data: <code>&#123;data&#125;</code></li>
 *     <li>wildcards: <code>*</code></li>
 *     <li>units: <code>tokenA</code></li>
 *     <li>identifiers: <code>identifier:tokenA</code></li>
 * </ul>
 *
 * Example: <br><br>
 *
 * <code>(send msg:[message] wildcard:* 3:to (console|terminalIdentifier:terminal[ ][screen *])|rand)</code><br><br>
 *
 * @param <T> the type of the expected return values processed by {@link org.panda_lang.framework.language.interpreter.pattern.lexical.processed.WildcardProcessor}
 */
public final class LexicalPattern<T> {

    private final LexicalPatternElement pattern;
    private @Nullable WildcardProcessor<T> processor;

    public LexicalPattern(LexicalPatternElement elements, @Nullable WildcardProcessor<T> processor) {
        this.pattern = elements;
        this.processor = processor;
    }

    public LexicalExtractorResult<T> extract(LexicalExtractor<T> extractor, String phrase) {
        return extractor.extract(phrase);
    }

    public LexicalExtractorResult<T> extract(String phrase) {
        return extract(new DefaultLexicalExtractor<>(this), phrase);
    }

    public LexicalPattern<T> setWildcardProcessor(WildcardProcessor<T> processor) {
        this.processor = processor;
        return this;
    }

    public boolean hasWildcardProcessor() {
        return processor != null;
    }

    public @Nullable WildcardProcessor<T> getWildcardProcessor() {
        return processor;
    }

    public LexicalPatternElement getModel() {
        return pattern;
    }

    public static <T> LexicalPatternBuilder<T> builder() {
        return new LexicalPatternBuilder<>();
    }

    private static final class DefaultLexicalExtractor<T> implements LexicalExtractor<T> {

        private final LexicalPattern<T> pattern;

        public DefaultLexicalExtractor(LexicalPattern<T> pattern) {
            this.pattern = pattern;
        }

        @Override
        public LexicalExtractorResult<T> extract(String phrase) {
            return new LexicalExtractorWorker<T>(null).extract(pattern.getModel(), phrase);
        }

    }

}
