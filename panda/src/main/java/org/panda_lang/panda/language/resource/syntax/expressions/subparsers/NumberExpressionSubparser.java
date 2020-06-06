/*
 * Copyright (c) 2020 Dzikoysk
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
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.token.PandaSnippet;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberUtils;

public final class NumberExpressionSubparser implements ExpressionSubparser {

    private static final NumberParser PARSER = new NumberParser();

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new NumberWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "number";
    }

    private static final class NumberWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        private Snippet content;
        private TokenInfo period;

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenInfo token) {
            if (Separators.PERIOD.equals(token)) {
                this.period = token;
                return ExpressionResult.empty();
            }

            if (token.getType() != TokenTypes.UNKNOWN || !NumberUtils.isNumeric(token.getValue())) {
                return dispose();
            }

            if (content == null) {
                this.content = new PandaSnippet();
            }

            if (this.period != null) {
                content.addToken(period);
            }

            content.addToken(token);
            Expression expression;

            try {
                expression = PARSER.parse(context.getContext(), content);
            } catch (NumberFormatException e) {
                return null;
            }

            // remove previous result from stack
            if (period != null) {
                context.popExpression();
                dispose();
            }

            return ExpressionResult.of(expression);
        }

        private @Nullable ExpressionResult dispose() {
            this.content = null;
            this.period = null;
            return null;
        }

    }

}
