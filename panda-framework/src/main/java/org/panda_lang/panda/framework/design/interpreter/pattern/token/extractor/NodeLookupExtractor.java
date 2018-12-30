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

import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.List;

class NodeLookupExtractor  {

    protected final NodeExtractor nodeExtractor;
    private final NodeElementLookupExtractor elementLookupExtractor = new NodeElementLookupExtractor(this);

    NodeLookupExtractor(NodeExtractor nodeExtractor) {
        this.nodeExtractor = nodeExtractor;
    }

    protected LookupResult extractNode(List<LexicalPatternElement> nextElements, TokenDistributor distributor) {
        int skip = 0;

        for (LexicalPatternElement nextElement : nextElements) {
            if (!nextElement.isWildcard()) {
                break;
            }

            if (!nextElement.toWildcard().isHole()) {
                break;
            }

            // wildcards with condition could be excluded in the future
            /*
            if (nextElement.toWildcard().hasCondition()) {
                break;
            }
            */

            skip++;
        }

        int indexBackup = distributor.getIndex();
        boolean matchable = true;

        for (int i = skip; i < nextElements.size(); i++) {
            LexicalPatternElement element = nextElements.get(i);
            distributor.setIndex(indexBackup);

            // consider exclusion of wildcards here

            LookupResult result = elementLookupExtractor.extractNode(nextElements.subList(0, skip), element, distributor);
            result.matchedIndex = i;

            if (result.getMergedResults().isMatched()) {
                return result;
            }

            if (element.isOptional()) {
                continue;
            }

            return result;
        }

        LookupResult lookupResult = new LookupResult();
        lookupResult.currentResult = new ExtractorResult();
        lookupResult.matchedIndex = nextElements.size();

        distributor.setIndex(indexBackup);
        lookupResult.precedingResult = elementLookupExtractor.extractWildcards(nextElements.subList(0, skip), distributor);

        return lookupResult;
    }

    static class LookupResult {

        protected ExtractorResult precedingResult;
        protected ExtractorResult currentResult;
        protected boolean notMatchable;
        protected int matchedIndex;

        protected ExtractorResult getMergedResults() {
            if (precedingResult == null || currentResult == null) {
                return new ExtractorResult("Undefined result (" + getMergedErrorMessage() + ")");
            }

            if (!precedingResult.isMatched() || !currentResult.isMatched()) {
                return new ExtractorResult("Lookup result not matched (" + getMergedErrorMessage() + ")");
            }

            return precedingResult.merge(currentResult);
        }

        private String getMergedErrorMessage() {
            String error = "";

            if (currentResult != null) {
                error += currentResult.getErrorMessage();
            }

            if (precedingResult != null) {
                if (!StringUtils.isEmpty(error)) {
                    error += "; ";
                }

                error += precedingResult.getErrorMessage();
            }

            if (StringUtils.isEmpty(error)) {
                error = "<unknown>";
            }

            return error;
        }

    }

}
