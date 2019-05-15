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
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.CreaseExpressionCallback;
import org.panda_lang.panda.framework.language.resource.syntax.operator.CreaseType;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

public class CreaseExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new CreaseWorker();
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "crease";
    }

    private static class CreaseWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            CreaseType type = null;

            if (context.getCurrentRepresentation().contentEquals(Operators.INCREMENT)) {
                type = CreaseType.INCREASE;
            }
            else if (context.getCurrentRepresentation().contentEquals(Operators.DECREMENT)) {
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
                expression = context.getParser().parse(context.getData(), context.getDiffusedSource());
            }

            if (!(expression instanceof AccessorExpression)) {
                return ExpressionResult.error("Expression is not associated with any variable", context.getCurrentRepresentation());
            }

            return ExpressionResult.of(new CreaseExpressionCallback(((AccessorExpression) expression).getAccessor(), type == CreaseType.INCREASE, post).toExpression());
        }

    }

}
