/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.design.interpreter.pattern.descriptive;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.TokenExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.condition.defaults.DefaultWildcardConditionFactories;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.wildcard.reader.defaults.DefaultWildcardReaders;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Collection;

public class DescriptivePattern {

    private final LexicalPatternElement patternContent;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = DefaultWildcardConditionFactories.getDefaultFactories();
    private final Collection<WildcardReader> wildcardReaders = DefaultWildcardReaders.getDefaultReaders();

    DescriptivePattern(DescriptivePatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    public ExtractorResult extract(ParserData data, Snippet source) {
        return extractor().extract(data, new PandaSourceStream(source));
    }

    public ExtractorResult extract(ParserData data, SourceStream source) {
        return extractor().extract(data, source);
    }

    public TokenExtractor extractor() {
        return new TokenExtractor(this);
    }

    public DescriptivePattern addWildcardConditionFactories(Collection<? extends WildcardConditionFactory> factories) {
        wildcardConditionFactories.addAll(factories);
        return this;
    }

    public DescriptivePattern addWildcardConditionFactory(WildcardConditionFactory factory) {
        wildcardConditionFactories.add(factory);
        return this;
    }

    public DescriptivePattern addWildcardReaders(Collection<? extends WildcardReader> readers) {
        wildcardReaders.addAll(readers);
        return this;
    }

    public DescriptivePattern addWildcardReader(WildcardReader reader) {
        wildcardReaders.add(reader);
        return this;
    }

    public String asString() {
        return patternContent.toString();
    }

    public Collection<? extends WildcardReader> getWildcardReaders() {
        return wildcardReaders;
    }

    public Collection<? extends WildcardConditionFactory> getWildcardConditionFactories() {
        return wildcardConditionFactories;
    }

    public LexicalPatternElement getPatternContent() {
        return patternContent;
    }

    public static DescriptivePatternBuilder builder() {
        return new DescriptivePatternBuilder();
    }

}
