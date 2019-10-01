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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.language.resource.syntax.operator.CreaseType;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;

public final class CreaseExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new CreaseWorker().withSubparser(this);
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "crease";
    }

    private static final class CreaseWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
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
            Expression expression;

            if (post) {
                expression = context.popExpression();
            }
            else {
                ExpressionTransaction transaction = context.getParser().parse(context.getContext(), context.getSynchronizedSource());
                context.commit(transaction::rollback);
                expression = transaction.getExpression();
            }

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
