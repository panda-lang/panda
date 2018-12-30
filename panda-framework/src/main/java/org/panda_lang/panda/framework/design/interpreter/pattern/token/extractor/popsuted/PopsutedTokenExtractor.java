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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.popsuted;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.ArrayList;
import java.util.Collection;

class PopsutedTokenExtractor {

    private final TokenPattern pattern;
    private final Collection<WildcardConditionFactory> wildcardConditionFactories = new ArrayList<>();

    public PopsutedTokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public TokenExtractorResult extract(Tokens source) {
        return extract(new PandaSourceStream(source));
    }

    public TokenExtractorResult extract(SourceStream source) {
        TokenExtractorWorker worker = new TokenExtractorWorker(pattern);
        return worker.extract(source);
    }

}
