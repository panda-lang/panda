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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternNode;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

import java.util.List;

class NodeExtractor extends AbstractElementExtractor<LexicalPatternNode> {

    private final NodeLookupExtractor nodeLookupExtractor = new NodeLookupExtractor(this);

    protected NodeExtractor(ExtractorWorker worker) {
        super(worker);
    }

    @Override
    public ExtractorResult extract(LexicalPatternNode node, TokenDistributor distributor) {
        List<LexicalPatternElement> elements = node.getElements();
        ExtractorResult result = new ExtractorResult();
        int matches = 0;

        for (int i = 0; i < elements.size(); i++) {
            LexicalPatternElement element = elements.get(i);
            int index = distributor.getIndex();

            ExtractorResult workerResult;

            if (!element.isWildcard()) {
                workerResult = extract(element, distributor);

                // out of source, but element was optional
                if (workerResult == null) {
                    break;
                }
            }
            else {
                NodeLookupExtractor.LookupResult lookupResult = nodeLookupExtractor.extractNode(elements.subList(i, elements.size()), distributor);
                workerResult = lookupResult.getMergedResults();

                // skip elements only if matched
                if (workerResult.isMatched()) {
                    i += lookupResult.matchedIndex;
                }
            }

            if (!workerResult.isMatched()) {
                // restore index for the next searches
                distributor.setIndex(index);

                if (element.isOptional()) {
                    continue;
                }

                return new ExtractorResult("Could not extract element, caused by: " + workerResult.getErrorMessage());
            }

            result.merge(workerResult);
            matches++;
        }

        if (matches == 0) {
            // return new ExtractorResult("Cannot match node, 0 matches");
        }

        return result;
    }

    /**
     * @param element the element to match
     * @param distributor source
     * @return subresult or null if distributor does not contains source
     */
    private @Nullable ExtractorResult extract(LexicalPatternElement element, TokenDistributor distributor) {
        if (!distributor.hasNext()) {
            if (element.isOptional()) {
                return null;
            }

            return new ExtractorResult("Out of source");
        }

        return super.getWorker().extract(distributor, element);
    }

}
