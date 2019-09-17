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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.array.ArrayAccessor;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.array.ArrayValueAccessorParser;
import org.panda_lang.panda.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.language.resource.syntax.separator.Separators;

public final class ArrayValueExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new ArrayValueWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "array-value";
    }

    private static final class ArrayValueWorker extends AbstractExpressionSubparserWorker {

        private static final ArrayValueAccessorParser PARSER = new ArrayValueAccessorParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            // require instance
            if (!context.hasResults()) {
                return null;
            }

            // require section token
            if (token.getType() != TokenType.SECTION) {
                return null;
            }

            Section section = token.toToken();

            // require square bracket section
            if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
                return null;
            }

            // require index
            if (section.getContent().isEmpty()) {
                return ExpressionResult.error("Missing index expression", section.getOpeningSeparator());
            }

            Expression instance = context.getResults().pop();

            // expression cannot be null
            if (instance.isNull()) {
                return null;
            }

            // require array type
            if (!instance.getReturnType().isArray()) {
                ExpressionResult.error("Cannot use array index on non-array return type", section.getContent());
            }

            ArrayClassPrototype arrayClassPrototype = (ArrayClassPrototype) instance.getReturnType();
            Expression indexExpression = context.getParser().parse(context.getContext(), section.getContent());

            // require int as index
            if (!PandaTypes.INT.isAssignableFrom(indexExpression.getReturnType())) {
                return ExpressionResult.error("Index of array has to be Integer", section.getContent());
            }

            // access the value
            ArrayAccessor accessor = PARSER.of(context.getContext(), section.getContent(), instance, indexExpression);
            return ExpressionResult.of(accessor.toExpression());
        }

    }

}
