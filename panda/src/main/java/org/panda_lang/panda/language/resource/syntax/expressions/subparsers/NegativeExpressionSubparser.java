/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberType;
import org.panda_lang.utilities.commons.ClassUtils;

import java.security.InvalidParameterException;

public final class NegativeExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new NegateWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String getSubparserName() {
        return "negative";
    }

    private static final class NegateWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (!token.contentEquals(Operators.SUBTRACTION)) {
                return null;
            }

            ExpressionTransaction transaction = context.getParser().parse(context.getContext(), context.getSynchronizedSource());
            context.commit(transaction::rollback);

            Expression expression = transaction.getExpression();
            Class<?> numberClass = ClassUtils.getNonPrimitiveClass(expression.getType().getAssociatedClass().getImplementation());

            if (!Number.class.isAssignableFrom(numberClass)) {
                throw new InvalidParameterException("Cannot reverse non logical value");
            }

            NegativeExpression negativeExpression = new NegativeExpression(expression, NumberType.of(numberClass));
            return ExpressionResult.of(negativeExpression.toExpression());
        }

    }

}
