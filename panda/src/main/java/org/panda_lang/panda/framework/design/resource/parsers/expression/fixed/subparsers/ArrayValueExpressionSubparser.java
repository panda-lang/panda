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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array.ArrayValueAccessor;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array.ArrayValueAccessorUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Stack;

public class ArrayValueExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new ArrayValueWorker();
    }

    static class ArrayValueWorker implements ExpressionSubparserWorker {

        private SeparatedContentReader contentReader;

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionParser parser, ParserData data, TokenRepresentation token, Stack<Expression> results) {
            if (contentReader == null) {
                if (results.isEmpty()) {
                    return ExpressionResult.empty();
                }

                Expression instance = results.peek();

                if (instance.getReturnType() == null || !instance.getReturnType().isArray()) {
                    return ExpressionResult.empty();
                }

                if (!Separators.SQUARE_BRACKET_LEFT.equals(token.getToken())) {
                    return ExpressionResult.empty();
                }

                this.contentReader = new SeparatedContentReader(Separators.SQUARE_BRACKET_LEFT, SeparatedContentReader.ContentProcessor.DEFAULT);
            }

            ExpressionResult<Expression> result = contentReader.read(parser, data, token);

            if (!contentReader.isDone() || result == null || result.containsError()) {
                return result;
            }

            Expression instanceExpression = results.pop();
            Expression indexExpression = result.get();

            ArrayValueAccessor.ArrayValueAccessorAction action = (branch, prototype, type, array, index) -> new PandaValue(type, array[index.intValue()]);
            ArrayValueAccessor accessor = ArrayValueAccessorUtils.of(data, contentReader.getContent(), instanceExpression, indexExpression, action);

            return ExpressionResult.of(accessor.toCallback().toExpression());
        }

    }

}
