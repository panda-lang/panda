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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.ProcessedValue;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed.WildcardProcessor;
import org.panda_lang.panda.utilities.commons.arrays.ArrayDistributor;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.List;
import java.util.Stack;

public class LexicalExtractorWorker<T> {

    private final @Nullable WildcardProcessor<T> wildcardProcessor;

    public LexicalExtractorWorker(@Nullable WildcardProcessor<T> wildcardProcessor) {
        this.wildcardProcessor = wildcardProcessor;
    }

    public LexicalExtractorResult<T> extract(LexicalPatternElement pattern, String phrase) {
        if (pattern.isUnit()) {
            boolean matched = phrase.equals(pattern.toUnit().getValue());

            if (!matched) {
                return new LexicalExtractorResult<>(false);
            }

            return new LexicalExtractorResult<T>(true).addIdentifier(pattern.getIdentifier());
        }

        if (pattern.isWildcard()) {
            LexicalPatternWildcard wilcardElement = pattern.toWildcard();
            String wildcard = phrase.trim();

            if (wildcardProcessor != null) {
                T result = wildcardProcessor.handle(wilcardElement.getDetails(), wildcard);

                if (result == null) {
                    return new LexicalExtractorResult<>(false);
                }

                return new LexicalExtractorResult<T>(true)
                        .addProcessedValue(new ProcessedValue<>(result, pattern.getIdentifier()))
                        .addIdentifier(pattern.getIdentifier())
                        .addWildcard(wildcard);
            }

            return new LexicalExtractorResult<T>(true)
                    .addIdentifier(pattern.getIdentifier())
                    .addWildcard(wildcard);
        }

        LexicalPatternNode node = pattern.toNode();

        if (node.isVariant()) {
            return this.matchVariant(node, phrase);
        }

        List<LexicalPatternElement> elements = node.getElements();
        String[] dynamics = this.matchUnits(phrase, elements);

        if (dynamics == null) {
            return new LexicalExtractorResult<>(false);
        }

        return this.matchDynamics(elements, dynamics);
    }

    private @Nullable String[] matchUnits(String phrase, List<LexicalPatternElement> elements) {
        Stack<LexicalPatternUnit> units = new Stack<>();
        String[] dynamics = new String[elements.size()];
        int index = 0;

        for (int i = index; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);

            if (!element.isUnit()) {
                continue;
            }

            LexicalPatternUnit unit = element.toUnit();
            units.push(unit);

            ArrayDistributor<LexicalPatternUnit> unitArrayDistributor = new ArrayDistributor<>(units, LexicalPatternUnit.class);
            unitArrayDistributor.reverse();
            int unitIndex = -1;

            for (LexicalPatternUnit currentUnit : unitArrayDistributor) {
                if (index < 0) {
                    return null;
                }

                if (StringUtils.isEmpty(unit.getValue())) {
                    if (!unit.isOptional() && unit.getIsolationType().isAny()) {
                        index++;
                    }

                    continue;
                }

                LexicalPatternUnit previousUnit = unitArrayDistributor.getPrevious();

                if (currentUnit.equals(previousUnit)) {
                    previousUnit = null;
                }

                boolean isolation = unit.getIsolationType().isStart() || (previousUnit != null && previousUnit.getIsolationType().isEnd());
                unitIndex = phrase.indexOf(unit.getValue(), index + (isolation ? 1 : 0));

                if (unitIndex != -1) {
                    break;
                }

                if (unit.isOptional()) {
                    break;
                }

                unitIndex = -2;

                if (previousUnit == null) {
                    break;
                }

                index -= previousUnit.getValue().length();
            }

            if (unitIndex == -2) {
                return null;
            }

            if (unitIndex == -1) {
                continue;
            }

            String before = phrase.substring(index, unitIndex).trim();

            if (unit.isOptional() && i + 1 < elements.size() && !StringUtils.isEmpty(before)) {
                LexicalPatternElement nextElement = elements.get(i + 1);
                LexicalExtractorResult<T> result = this.extract(nextElement, before);

                if (result.isMatched()) {
                    // TODO: Advanced research
                    unitIndex = -unit.getValue().length() + index;
                    before = null;
                }
            }

            if (!StringUtils.isEmpty(before)) {
                if (i - 1 < 0) {
                    return null;
                }

                dynamics[i - 1] = before;
            }

            index = unitIndex + unit.getValue().length();
        }

        if (index < phrase.length()) {
            dynamics[dynamics.length - 1] = phrase.substring(index, phrase.length());
        }

        return dynamics;
    }

    private LexicalExtractorResult<T> matchDynamics(List<LexicalPatternElement> elements, String[] dynamics) {
        LexicalExtractorResult<T> result = new LexicalExtractorResult<>(true);

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement nodeElement = elements.get(i);

            if (nodeElement.isUnit()) {
                continue;
            }

            if (dynamics.length == 0 && nodeElement.isOptional()) {
                continue;
            }

            String nodeContent = dynamics[i];
            dynamics[i] = null;

            if (nodeContent == null) {
                return new LexicalExtractorResult<>(false);
            }

            LexicalExtractorResult<T> nodeElementResult = this.extract(nodeElement, nodeContent);

            if (!nodeElementResult.isMatched()) {
                return new LexicalExtractorResult<>(false);
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (String dynamicContent : dynamics) {
            if (!StringUtils.isEmpty(dynamicContent)) {
                return new LexicalExtractorResult<>(false);
            }
        }

        return result;
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

        return new LexicalExtractorResult<>(false);
    }

}
