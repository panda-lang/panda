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

package org.panda_lang.panda.framework.design.resource.parsers.expression.utils.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

public class ExpressionSeparatorReader {

    private static final ExpressionSeparatorReader INSTANCE = new ExpressionSeparatorReader();

    public @Nullable Tokens readSeparated(ExpressionParser main, Tokens source, Token[] separators, ExpressionSeparatorExtensions extensions) {
        TokenDistributor distributor = new TokenDistributor(source);
        int lastIndexOfPeriod = -1;

        MatchableDistributor matchable = new MatchableDistributor(distributor);
        matchable.verify();

        while (matchable.hasNext()) {
            TokenRepresentation representation = matchable.next();
            matchable.verify();

            if (!matchable.isMatchable()) {
                continue;
            }

            if (!TokenUtils.contains(separators, representation.getToken())) {
                if (extensions.testToken(representation.getToken())) {
                    break;
                }

                continue;
            }

            Tokens selected = source.subSource(0, matchable.getIndex() - 1);
            Tokens matched = main.read(selected);

            if (matched == null || matched.size() != selected.size()) {
                break;
            }

            if (extensions.testSection(matched)) {
                break;
            }

            lastIndexOfPeriod = matchable.getIndex() - 1;
        }

        if (lastIndexOfPeriod == -1) {
            return null;
        }

        distributor.setIndex(lastIndexOfPeriod);

        if (!matchable.hasNext()) {
            return null;
        }

        boolean result = extensions.finisher.finish(main, matchable);

        if (!result) {
            return null;
        }

        return source.subSource(0, matchable.getIndex());
    }

    public static ExpressionSeparatorReader getInstance() {
        return INSTANCE;
    }

}
