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
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.parsers.general.number.NumberParser;
import org.panda_lang.panda.framework.language.resource.parsers.general.number.NumberUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.util.Stack;

public class NumberExpressionSubparser implements ExpressionSubparser {

    private static final NumberParser PARSER = new NumberParser();

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new NumberWorker();
    }

    static class NumberWorker implements ExpressionSubparserWorker {

        private Tokens content;
        private TokenRepresentation period;
        private Expression previous;

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionParser parser, ParserData data, TokenRepresentation token, Stack<Expression> results) {
            if (token.getType() == TokenType.SEPARATOR) {
                if (!token.contentEquals(Separators.PERIOD)) {
                    return null;
                }

                period = token;
                return ExpressionResult.empty();
            }

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            if (content == null) {
                this.content = new PandaTokens();
            }

            // check saved with new token
            if (NumberUtils.isNumeric(token.getTokenValue())) {
                if (period != null) {
                    content.addToken(period);
                    period = null;
                }

                content.addToken(token);
            }
            else {
                return null;
            }

            Value numericValue = PARSER.parse(data, content);

            if (numericValue == null) {
                return null;
            }

            Expression expression = new PandaExpression(numericValue);

            // remove previous result from stack
            if (!results.isEmpty() && results.peek() == previous) {
                results.pop();
            }

            previous = expression;
            return ExpressionResult.of(expression);
        }

    }

}
