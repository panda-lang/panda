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
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.WildcardCompiler;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

class WildcardExtractor extends AbstractElementExtractor<LexicalPatternWildcard> {

    private final WildcardCompiler wildcardCompiler;

    protected WildcardExtractor(ExtractorWorker worker) {
        super(worker);
        this.wildcardCompiler = new WildcardCompiler(worker.pattern);
    }

    @Override
    public ExtractorResult extract(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        int index = distributor.getIndex();
        Tokens wildcardContent = null;

        if (!distributor.hasNext()) {
            wildcardContent = new PandaTokens();
        }
        else if (wildcard.getData() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);

            if (wildcardContent == null) {
                wildcardContent = new PandaTokens(distributor.next());
            }

            if (wildcardContent.isEmpty()) {
                return new ExtractorResult("Empty wildcard");
            }
        }

        int wildcardSize = wildcardContent != null ? wildcardContent.size() : 0;
        distributor.setIndex(index + wildcardSize);

        return new ExtractorResult().addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Tokens matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        if (wildcard.getName().startsWith("*")) {
            return new PandaTokens(distributor.next(distributor.size() - distributor.getIndex()));
        }

        if (!wildcard.hasCondition()) {
            return null;
        }

        return wildcardCompiler.compile(wildcard.getCondition(), distributor);
    }

}
