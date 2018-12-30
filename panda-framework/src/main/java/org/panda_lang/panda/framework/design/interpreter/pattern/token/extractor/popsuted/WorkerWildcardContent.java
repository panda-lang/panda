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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.pattern.lexical.elements.LexicalPatternWildcard;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardCondition;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

import java.util.ArrayList;
import java.util.List;

class WorkerWildcardContent {

    private final TokenExtractorWorker worker;

    WorkerWildcardContent(TokenExtractorWorker worker) {
        this.worker = worker;
    }

    protected TokenExtractorResult matchWildcard(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        Tokens wildcardContent = null;

        if (wildcard.getCondition() != null) {
            wildcardContent = matchWildcardWithCondition(wildcard, distributor);
        }

        if (wildcardContent == null) {
            wildcardContent = new PandaTokens(distributor.next());
        }

        return new TokenExtractorResult(true).addWildcard(wildcard.getName(), wildcardContent);
    }

    private @Nullable Tokens matchWildcardWithCondition(LexicalPatternWildcard wildcard, TokenDistributor distributor) {
        if (wildcard.getName().startsWith("*")) {
            return new PandaTokens(distributor.next(distributor.size() - distributor.getIndex()));
        }

        if (!wildcard.hasCondition()) {
            return null;
        }

        String[] conditions = wildcard.getCondition().split(",");
        List<WildcardCondition> wildcardConditions = new ArrayList<>(conditions.length);

        for (String condition : conditions) {
            WildcardCondition wildcardCondition = createWildcardCondition(condition);

            if (wildcardCondition == null) {
                throw new PandaFrameworkException("Unknown wildcard condition: " + condition);
            }

            wildcardConditions.add(wildcardCondition);
        }

        List<TokenRepresentation> tokens = new ArrayList<>(distributor.size() - distributor.getIndex());

        while (distributor.hasNext()) {
            TokenRepresentation next = distributor.getNext();

            if (!checkWildcard(wildcardConditions, next)) {
                break;
            }

            tokens.add(distributor.next());
        }

        return new PandaTokens(tokens);
    }

    private @Nullable WildcardCondition createWildcardCondition(String condition) {
        condition = condition.trim();

        for (WildcardConditionFactory factory : worker.pattern.getWildcardConditionFactories()) {
            if (factory.handle(condition)) {
                return factory.create(condition);
            }
        }

        return null;
    }

    private boolean checkWildcard(List<WildcardCondition> wildcardConditions, TokenRepresentation next) {
        for (WildcardCondition wildcardCondition : wildcardConditions) {
            /*
            if (wildcardCondition.accept(next)) {
                return true;
            }
            */
        }

        return false;
    }

}
