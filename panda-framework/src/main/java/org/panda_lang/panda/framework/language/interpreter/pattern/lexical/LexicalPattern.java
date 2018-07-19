/*
 * Copyright (c) 2015-2018 Dzikoysk
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

import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractor;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.LexicalExtractorResult;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.WildcardProcessor;

import java.util.Collection;

public class LexicalPattern<T> {

    private final LexicalPatternElement pattern;
    private final Collection<WildcardProcessor<T>> processors;

    public LexicalPattern(LexicalPatternElement elements, Collection<WildcardProcessor<T>> processors) {
        this.pattern = elements;
        this.processors = processors;
    }

    public LexicalExtractorResult<T> extract(String phrase) {
        LexicalExtractor<T> extractor = new LexicalExtractor<>(this);
        return extractor.extract(phrase);
    }

    public Collection<WildcardProcessor<T>> getProcessors() {
        return processors;
    }

    public LexicalPatternElement getModel() {
        return pattern;
    }

    public static <T> LexicalPatternBuilder<T> builder() {
        return new LexicalPatternBuilder<>();
    }

}
