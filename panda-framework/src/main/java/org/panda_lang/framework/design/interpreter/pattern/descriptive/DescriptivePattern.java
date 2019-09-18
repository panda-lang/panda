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

package org.panda_lang.framework.design.interpreter.pattern.descriptive;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor.TokenExtractor;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.condition.defaults.DefaultWildcardConditionFactories;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.reader.defaults.DefaultWildcardReaders;
import org.panda_lang.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;

import java.util.Collection;

/**
 * DescriptivePattern is based on {@link org.panda_lang.framework.design.interpreter.pattern.lexical.LexicalPattern}, enhanced by: <br>
 *
 * <ul>
 *     <li>wildcard condition factories: {@link org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.condition.WildcardConditionFactory}</li>
 *     <li>wildcard readers: {@link org.panda_lang.framework.design.interpreter.pattern.descriptive.wildcard.reader.WildcardReader}</li>
 * </ul>
 *
 * Examples: <br><br>
 *
 * <code>&#123;type:reader type&#125; &#123;name&#125;</code><br>
 * <code>&#123;source:reader expression include field&#125;</code><br>
 * <code>module &#123;module:condition token {type:unknown}, token {value:-}&#125;[;]</code><br><br>
 *
 */
public class DescriptivePattern {

    private final LexicalPatternElement patternContent;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = DefaultWildcardConditionFactories.getDefaultFactories();
    private final Collection<WildcardReader> wildcardReaders = DefaultWildcardReaders.getDefaultReaders();

    DescriptivePattern(DescriptivePatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    public ExtractorResult extract(Context context, Snippet source) {
        return extractor().extract(context, new PandaSourceStream(source));
    }

    public ExtractorResult extract(Context context, SourceStream source) {
        return extractor().extract(context, source);
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
