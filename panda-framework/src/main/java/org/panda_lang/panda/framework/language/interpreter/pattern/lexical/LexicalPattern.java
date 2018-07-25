/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorResult;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorWorker;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.WildcardProcessor;

public class LexicalPattern<T> {

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

    private static class DefaultLexicalExtractor<T> implements LexicalExtractor<T> {

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
