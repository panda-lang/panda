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

package org.panda_lang.framework.design.interpreter.pattern.lexical.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.framework.design.interpreter.pattern.lexical.elements.LexicalPatternUnit;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.iterable.ArrayDistributor;

import java.util.List;
import java.util.Stack;

final class LexicalExtractorWorkerDynamics<T> {

    private final LexicalExtractorWorker<T> worker;

    public LexicalExtractorWorkerDynamics(LexicalExtractorWorker<T> worker) {
        this.worker = worker;
    }

    protected @Nullable String[] extractDynamics(String phrase, List<LexicalPatternElement> elements) {
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
            int unitIndex = LexicalExtractorWorker.NOT_FOUND;

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

                if (unitIndex != LexicalExtractorWorker.NOT_FOUND) {
                    break;
                }

                if (unit.isOptional()) {
                    break;
                }

                unitIndex = LexicalExtractorWorker.INVALID;

                if (previousUnit == null) {
                    break;
                }

                index -= previousUnit.getValue().length();
            }

            if (unitIndex == LexicalExtractorWorker.NOT_FOUND) {
                continue;
            }

            if (unitIndex == LexicalExtractorWorker.INVALID) {
                return null;
            }

            String before = phrase.substring(index, unitIndex).trim();

            if (unit.isOptional() && i + 1 < elements.size() && !StringUtils.isEmpty(before)) {
                LexicalPatternElement nextElement = elements.get(i + 1);
                LexicalExtractorResult<T> result = worker.extract(nextElement, before);

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
            dynamics[dynamics.length - 1] = phrase.substring(index);
        }

        return dynamics;
    }

    protected LexicalExtractorResult<T> matchDynamics(List<LexicalPatternElement> elements, String[] dynamics) {
        LexicalExtractorResult<T> result = new LexicalExtractorResult<>();

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
                return LexicalExtractorResult.err();
            }

            LexicalExtractorResult<T> nodeElementResult = worker.extract(nodeElement, nodeContent);

            if (!nodeElementResult.isMatched()) {
                return LexicalExtractorResult.err();
            }

            result.addIdentifier(nodeElement.getIdentifier());
            result.merge(nodeElementResult);
        }

        for (String dynamicContent : dynamics) {
            if (!StringUtils.isEmpty(dynamicContent)) {
                return LexicalExtractorResult.err();
            }
        }

        return result;
    }

}
