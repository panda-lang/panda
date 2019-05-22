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
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.ContentProcessor;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.SeparatedContentReader;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class MethodExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new MethodWorker();
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "method";
    }

    private static class MethodWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            Token token = context.getCurrentRepresentation().getToken();

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            String methodName = token.getTokenValue();

            if (!context.getDiffusedSource().hasNext() || !context.getDiffusedSource().getNext().contentEquals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            Expression instance;

            if (context.hasResults() && TokenUtils.contentEquals(context.getDiffusedSource().getPrevious(), Separators.PERIOD)) {
                instance = context.peekExpression();
            }
            else if (context.getDiffusedSource().getIndex() == 1) {
                instance = ThisExpressionCallback.of(context.getData());
            }
            else {
                return null;
            }

            if (!instance.getReturnType().getMethods().hasMethodLike(methodName)) {
                return ExpressionResult.error("Cannot find method called '" + methodName + "'", context.getCurrentRepresentation());
            }

            SeparatedContentReader parametersReader = new SeparatedContentReader(Separators.PARENTHESIS_LEFT, ContentProcessor.NON_PROCESSING);
            TokenRepresentation separator = context.getDiffusedSource().next();
            ExpressionResult result = parametersReader.read(context);

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
