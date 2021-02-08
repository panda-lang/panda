/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionParserSettings;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.resource.syntax.operator.Operators;

public final class NegateParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new NegateWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String name() {
        return "negate";
    }

    private static final class NegateWorker extends AbstractExpressionSubparserWorker {

        private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
                .mutualDisabled()
                .build();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!token.contentEquals(Keywords.NOT) && !token.contentEquals(Operators.NOT)) {
                return null;
            }

            Expression expression = context.getParser().parse(context.toContext(), context.getSynchronizedSource(), SETTINGS);
            return ExpressionResult.of(new NegateExpression(expression).toExpression());
        }

    }

}
