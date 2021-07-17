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

package panda.interpreter.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.dynamic.accessor.AccessorExpression;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionCategory;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionParserSettings;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.operator.CreaseType;
import panda.interpreter.resource.syntax.operator.Operators;

public final class CreaseParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new CreaseWorker().withSubparser(this);
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String name() {
        return "crease";
    }

    private static final class CreaseWorker extends AbstractExpressionSubparserWorker {

        private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
                .mutualDisabled()
                .build();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            CreaseType type = null;

            if (token.contentEquals(Operators.INCREMENT)) {
                type = CreaseType.INCREASE;
            }
            else if (token.contentEquals(Operators.DECREMENT)) {
                type = CreaseType.DECREASE;
            }

            if (type == null) {
                return null;
            }

            boolean post = context.hasResults();

            Expression expression = post
                    ? context.popExpression()
                    : context.getParser().parse(context.toContext(), context.getSynchronizedSource(), SETTINGS);

            if (!(expression instanceof AccessorExpression)) {
                return ExpressionResult.error("Expression is not associated with any variable", token);
            }

            AccessorExpression accessorExpression = (AccessorExpression) expression;

            if (!accessorExpression.getAccessor().getVariable().isMutable()) {
                return ExpressionResult.error("Cannot modify immutable variable", token);
            }

            return ExpressionResult.of(new CreaseExpression(((AccessorExpression) expression).getAccessor(), type == CreaseType.INCREASE, post).toExpression());
        }

    }

}
