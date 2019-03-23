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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor;

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

public class ExtractorWorker {

    public static long fullTime;

    protected final TokenPattern pattern;
    protected final UnitExtractor unitExtractor;
    protected final WildcardExtractor wildcardExtractor;
    protected final VariantExtractor variantExtractor;
    protected final NodeExtractor nodeExtractor;

    ExtractorWorker(TokenPattern pattern) {
        this.pattern = pattern;
        this.unitExtractor = new UnitExtractor(this);
        this.wildcardExtractor = new WildcardExtractor(this);
        this.variantExtractor = new VariantExtractor(this);
        this.nodeExtractor = new NodeExtractor(this);
    }

    protected ExtractorResult extract(SourceStream source) {
        long time = System.nanoTime();

        TokenDistributor distributor = new TokenDistributor(source.toSnippet());
        ExtractorResult result = extract(distributor, pattern.getPatternContent());

        if (result.isMatched()) {
            source.read(distributor.getIndex());
        }

        fullTime += System.nanoTime() - time;
        return result;
    }

    protected ExtractorResult extract(TokenDistributor distributor, LexicalPatternElement element) {
        return extractInternal(distributor, element).identified(element.getIdentifier());
    }

    private ExtractorResult extractInternal(TokenDistributor distributor, LexicalPatternElement element) {
        if (element.isUnit()) {
            return unitExtractor.extract(element.toUnit(), distributor);
        }

        if (element.isWildcard()) {
            return wildcardExtractor.extract(element.toWildcard(), distributor);
        }

        if (element.isVariant()) {
            return variantExtractor.extract(element.toNode(), distributor);
        }

        if (element.isNode()) {
            return nodeExtractor.extract(element.toNode(), distributor);
        }

        return new ExtractorResult("Unknown element: " + element);
    }

}
