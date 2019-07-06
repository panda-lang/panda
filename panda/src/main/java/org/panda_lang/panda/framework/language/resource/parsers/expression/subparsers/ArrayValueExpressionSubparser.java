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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.array.ArrayValueAccessor;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.array.ArrayValueAccessorUtils;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ArrayValueExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ArrayValueWorker();
    }

    @Override
    public String getSubparserName() {
        return "array-value";
    }

    private static class ArrayValueWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            if (!context.hasResults()) {
                return null;
            }

            Expression instance = context.getResults().peek();

            if (instance.getReturnType() == null) {
                return null;
            }

            if (context.getCurrentRepresentation().getType() != TokenType.SECTION) {
                return null;
            }

            Section section = context.getCurrentRepresentation().toToken();

            if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
                return null;
            }

            if (section.getContent().isEmpty()) {
                return ExpressionResult.error("Missing index expression", section.getOpeningSeparator());
            }

            Expression instanceExpression = context.getResults().pop();

            if (!instance.getReturnType().isArray()) {
                ExpressionResult.error("Cannot use array index on non-array return type", section.getContent());
            }

            Expression indexExpression = context.getParser().parse(context.getContext(), section.getContent());

            if (!PandaTypes.INT.isAssignableFrom(indexExpression.getReturnType())) {
                return ExpressionResult.error("Index of array has to be Integer", section.getContent());
            }

            ArrayValueAccessor.ArrayValueAccessorAction action = (branch, prototype, type, array, index) -> new PandaStaticValue(type, array[index]);
            ArrayValueAccessor accessor = ArrayValueAccessorUtils.of(context.getContext(), section.getContent(), instanceExpression, indexExpression, action);

            return ExpressionResult.of(accessor.toCallback().toExpression());
        }

    }

}
