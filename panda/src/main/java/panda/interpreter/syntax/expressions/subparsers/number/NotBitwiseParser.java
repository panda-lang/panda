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

package panda.interpreter.syntax.expressions.subparsers.number;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionParserSettings;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserType;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.operator.Operators;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;

import java.security.InvalidParameterException;

public final class NotBitwiseParser implements ExpressionSubparser {

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
        return "not-bitwise";
    }

    private static final class NegateWorker extends AbstractExpressionSubparserWorker {

        private static final ExpressionParserSettings SETTINGS = ExpressionParserSettings.create()
                .mutualDisabled()
                .build();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!token.contentEquals(Operators.BITWISE_NOT)) {
                return null;
            }

            Expression expression = context.getParser().parse(context.toContext(), context.getSynchronizedSource(), SETTINGS);

            if (!context.toContext().getTypeLoader().requireType("panda/panda@::Number").isAssignableFrom(expression.getKnownType())) {
                throw new InvalidParameterException("Cannot reverse non logical value");
            }

            NotBitwiseExpression negativeExpression = new NotBitwiseExpression(expression, NumberType.of(expression.getKnownType().getName()));
            return ExpressionResult.of(negativeExpression.toExpression());
        }

    }

}
