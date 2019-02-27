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
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.syntax.separator.SeparatorStack;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Stack;

public class SectionExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new SentenceWorker();
    }

    static class SentenceWorker implements ExpressionSubparserWorker {

        private Tokens content;
        private SeparatorStack separators;

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionParser parser, ParserData data, TokenRepresentation token, Stack<Expression> results) {
            if (isDone()) {
                return null;
            }

            // initialize
            if (content == null && separators == null) {
                if (!token.contentEquals(Separators.PARENTHESIS_LEFT)) {
                    return null;
                }

                this.separators = new SeparatorStack();
                this.content = new PandaTokens();
                return ExpressionResult.empty();
            }

            // not initialized
            if (content == null) {
                return null;
            }

            // exclude separators not related to the section
            boolean result = separators.check(token.getToken());

            if (!result && !separators.isLocked() && token.contentEquals(Separators.PARENTHESIS_RIGHT)) {
                separators = null;

                if (content.isEmpty()) {
                    return ExpressionResult.error("Expression expected", token);
                }

                return ExpressionResult.of(parser.parse(data, content));
            }

            content.addToken(token);
            return ExpressionResult.empty();
        }

        @Override
        public boolean isDone() {
            return content != null && separators == null;
        }

    }

}
