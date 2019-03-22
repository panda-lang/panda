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

package org.panda_lang.panda.framework.design.interpreter.pattern.token;

import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.TokenExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.defaults.DefaultWildcardConditionFactories;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.defaults.DefaultWildcardReaders;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Collection;

public class TokenPattern {

    private final LexicalPatternElement patternContent;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = DefaultWildcardConditionFactories.getDefaultFactories();
    private final Collection<WildcardReader> wildcardReaders = DefaultWildcardReaders.getDefaultReaders();

    TokenPattern(TokenPatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    public ExtractorResult extract(Snippet source) {
        return extractor().extract(new PandaSourceStream(source));
    }

    public ExtractorResult extract(SourceStream source) {
        return extractor().extract(source);
    }

    public TokenExtractor extractor() {
        return new TokenExtractor(this);
    }

    public TokenPattern addWildcardConditionFactories(Collection<? extends WildcardConditionFactory> factories) {
        wildcardConditionFactories.addAll(factories);
        return this;
    }

    public TokenPattern addWildcardConditionFactory(WildcardConditionFactory factory) {
        wildcardConditionFactories.add(factory);
        return this;
    }

    public TokenPattern addWildcardReaders(Collection<? extends WildcardReader> readers) {
        wildcardReaders.addAll(readers);
        return this;
    }

    public TokenPattern addWildcardReader(WildcardReader reader) {
        wildcardReaders.add(reader);
        return this;
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

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

}
