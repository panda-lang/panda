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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionContext;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.ContentProcessor;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.ReusableExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array.ArrayValueAccessor;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array.ArrayValueAccessorUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ArrayValueExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new ArrayValueWorker().withSubparser(this);
    }

    private static class ArrayValueWorker extends AbstractExpressionSubparserWorker implements ReusableExpressionSubparserWorker {

        private SeparatedContentReader contentReader;

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            if (contentReader == null) {
                if (!context.hasResults()) {
                    return null;
                }

                Expression instance = context.getResults().peek();

                if (instance.getReturnType() == null || !instance.getReturnType().isArray()) {
                    return null;
                }

                if (!Separators.SQUARE_BRACKET_LEFT.equals(context.getCurrent().getToken())) {
                    return null;
                }

                this.contentReader = new SeparatedContentReader(Separators.SQUARE_BRACKET_LEFT, ContentProcessor.DEFAULT);
            }

            ExpressionResult<Expression> result = contentReader.read(context);

            if (result == null || result.containsError()) {
                return result;
            }

            Expression instanceExpression = context.getResults().pop();
            Expression indexExpression = result.get();

            if (!PandaTypes.INT.isAssignableFrom(indexExpression.getReturnType())) {
                return ExpressionResult.error("Index of array has to be Integer", contentReader.getContent());
            }

            ArrayValueAccessor.ArrayValueAccessorAction action = (branch, prototype, type, array, index) -> new PandaValue(type, array[index.intValue()]);
            ArrayValueAccessor accessor = ArrayValueAccessorUtils.of(context.getData(), contentReader.getContent(), instanceExpression, indexExpression, action);
            contentReader = null;

            return ExpressionResult.of(accessor.toCallback().toExpression());
        }

    }

}
