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
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionContext;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.ContentProcessor;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.util.SeparatedContentReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class MethodExpressionSubparser implements ExpressionSubparser {

    private static final ContentProcessor CONTENT_PROCESSOR = (reader, context, content, last) -> null;

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new MethodWorker();
    }

    private static class MethodWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionContext context) {
            Token token = context.getCurrent().getToken();

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            String methodName = token.getTokenValue();

            if (!context.getDiffusedSource().hasNext() || !context.getDiffusedSource().getNext().contentEquals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            Expression instance;

            if (context.hasResults()) {
                instance = context.peekExpression();
            }
            else {
                instance = ThisExpressionCallback.of(context.getData());
            }

            if (!instance.getReturnType().getMethods().hasMethodLike(methodName)) {
                return null;
            }

            SeparatedContentReader parametersReader = new SeparatedContentReader(Separators.PARENTHESIS_LEFT, CONTENT_PROCESSOR);
            TokenRepresentation separator = context.getDiffusedSource().next();
            ExpressionResult<Expression> result = parametersReader.read(context);

            if (!parametersReader.hasContent()) {
                return ExpressionResult.error("Cannot read parameters", separator);
            }

            MethodInvokerExpressionParser methodParser = new MethodInvokerExpressionParser();
            methodParser.parse(context.getData(), instance, methodName, parametersReader.getContent());

            if (context.hasResults()) {
                context.popExpression();
            }

            return ExpressionResult.of(methodParser.toCallback().toExpression());
        }

    }

}
