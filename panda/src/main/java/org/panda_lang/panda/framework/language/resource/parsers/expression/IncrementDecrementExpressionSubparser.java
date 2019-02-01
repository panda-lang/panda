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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

public class IncrementDecrementExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        if (source.startsWith(Operators.INCREMENT) || source.startsWith(Operators.DECREMENT)) {
            Tokens pre = main.read(source.subSource(1, source.size()));

            if (pre == null) {
                return null;
            }

            return new PandaTokens(source.getFirst()).addTokens(pre);
        }

        ExpressionParser postExpression = main.getSubparsers().fork()
                .removeSubparser(getName())
                .toExpressionParser(null);

        Tokens post = postExpression.read(source);

        if (post == null) {
            return null;
        }

        if (post.size() == source.size()) {
            return null;
        }

        TokenRepresentation operator = source.get(post.size());

        if (!operator.contentEquals(Operators.INCREMENT) && !operator.contentEquals(Operators.DECREMENT)) {
            return null;
        }

        return post.addToken(operator);
    }

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        System.out.println("poggers");
        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.INCREMENT_DECREMENT;
    }

}
