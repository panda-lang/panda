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
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
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
        if (!wildcard.hasCondition() && !wildcard.getName().startsWith("*")) {
            wildcardContent = new PandaTokens(distributor.next());
        }
        else if (wildcard.getData() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);

            if (TokensUtils.isEmpty(wildcardContent)) {
                return new ExtractorResult("Empty wildcard with condition: " + wildcard.getData());
            }
        }

        int wildcardSize = wildcardContent != null ? wildcardContent.size() : 0;
        distributor.setIndex(index + wildcardSize);

        return new ExtractorResult().addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Tokens matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        Tokens source = null;
        boolean full = false;

        if (wildcard.getName().startsWith("*")) {
            source = new PandaTokens(distributor.next(distributor.size() - distributor.getIndex()));
            full = true;
        }

        if (!wildcard.hasCondition()) {
            return source;
        }

        if (source != null) {
            distributor = new TokenDistributor(source);
        }

        Tokens matched = wildcardCompiler.compile(wildcard.getCondition(), distributor);

        if (full && matched != null && matched.size() != source.size()) {
            return null;
        }

        return matched;
    }

}
