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

package org.panda_lang.framework.language.interpreter.pattern.lexical.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternSection;
import org.panda_lang.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.framework.language.interpreter.pattern.lexical.processed.ProcessedValue;
import org.panda_lang.framework.language.interpreter.pattern.lexical.processed.WildcardProcessor;
import org.panda_lang.framework.language.interpreter.token.PandaTokenRepresentation;

import java.util.List;

public final class LexicalExtractorWorker<T> {

    protected static final int NOT_FOUND = -1;
    protected static final int INVALID = -2;

    private final @Nullable WildcardProcessor<T> wildcardProcessor;
    private final LexicalExtractorWorkerDynamics<T> workerDynamics = new LexicalExtractorWorkerDynamics<>(this);

    public LexicalExtractorWorker(@Nullable WildcardProcessor<T> wildcardProcessor) {
        this.wildcardProcessor = wildcardProcessor;
    }

    public LexicalExtractorResult<T> extract(LexicalPatternElement pattern, String phrase) {
        return extractElement(pattern, phrase).withSource(PandaTokenRepresentation.of(TokenTypes.UNKNOWN, phrase));
    }

    private LexicalExtractorResult<T> extractElement(LexicalPatternElement pattern, String phrase) {
        if (pattern.isUnit()) {
            return matchUnit(pattern, phrase);
        }

        if (pattern.isWildcard()) {
            return extractWildcard(pattern.toWildcard(), phrase);
        }

        if (pattern.isSection()) {
            return extractSection(pattern.toSection(), phrase);
        }

        LexicalPatternNode node = pattern.toNode();

        if (node.isVariant()) {
            return this.matchVariant(node, phrase);
        }

        List<LexicalPatternElement> elements = node.getElements();
        String[] dynamics = workerDynamics.extractDynamics(phrase, elements);

        if (dynamics == null) {
            return LexicalExtractorResult.err();
        }

        return workerDynamics.matchDynamics(elements, dynamics);
    }

    private @Nullable LexicalExtractorResult<T> matchUnit(LexicalPatternElement pattern, String phrase) {
        return !phrase.equals(pattern.toUnit().getValue()) ? LexicalExtractorResult.err() : new LexicalExtractorResult<T>().addIdentifier(pattern.getIdentifier());
    }

    private LexicalExtractorResult<T> matchVariant(LexicalPatternNode variantNode, String phrase) {
        if (!variantNode.isVariant()) {
            throw new RuntimeException("The specified node is not marked as a variant node");
        }

        for (LexicalPatternElement variantElement : variantNode.getElements()) {
            LexicalExtractorResult<T> result = this.extract(variantElement, phrase);

            if (result.isMatched()) {
                return result;
            }
        }

        return LexicalExtractorResult.err();
    }

    private @Nullable LexicalExtractorResult<T> extractWildcard(LexicalPatternWildcard pattern, String phrase) {
        String wildcard = phrase.trim();

        if (wildcardProcessor == null) {
            return new LexicalExtractorResult<T>()
                    .addIdentifier(pattern.getIdentifier())
                    .addWildcard(wildcard);
        }

        T result = wildcardProcessor.handle(pattern.getCondition(), wildcard);

        if (result == null) {
            return LexicalExtractorResult.err();
        }

        return new LexicalExtractorResult<T>()
                .addProcessedValue(new ProcessedValue<>(result, pattern.getIdentifier()))
                .addIdentifier(pattern.getIdentifier())
                .addWildcard(wildcard);
    }

    private @Nullable LexicalExtractorResult<T> extractSection(LexicalPatternSection pattern, String phrase) {
        throw new RuntimeException("Not impl");
    }

}
