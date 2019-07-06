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
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.invoker.MethodInvoker;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.parsers.common.ArgumentsParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks.MethodInvokerExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.StaticExpression;

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

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            TokenRepresentation nameToken = context.getCurrentRepresentation();

            if (nameToken.getType() != TokenType.UNKNOWN) {
                return null;
            }

            String name = nameToken.getValue();

            if (!context.getDiffusedSource().hasNext() || context.getDiffusedSource().getNext().getType() != TokenType.SECTION) {
                return null;
            }
            
            if (!context.getDiffusedSource().getNext().toToken(Section.class).getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            Expression instance = null;

            if (context.hasResults() && TokenUtils.contentEquals(context.getDiffusedSource().getPrevious(), Separators.PERIOD)) {
                instance = context.peekExpression();
            }
            else if (context.getDiffusedSource().getIndex() == 1) {
                instance = ThisExpressionCallback.of(context.getContext());
            }

            if (instance == null) {
                return null;
            }

            if (!instance.getReturnType().getMethods().hasMethodLike(name)) {
                return ExpressionResult.error("Cannot find method called '" + name + "'", context.getCurrentRepresentation());
            }

            Snippet arguments = context.getDiffusedSource().next().toToken(Section.class).getContent();
            ExpressionCallback callback = parse(context.getContext(), instance, nameToken, arguments);

            if (context.hasResults()) {
                context.popExpression();
            }

            return ExpressionResult.of(callback.toExpression());
        }

        public ExpressionCallback parse(Context context, Expression instance, TokenRepresentation methodName, Snippet argumentsSource) {
            ClassPrototype prototype = instance.getReturnType();

            Expression[] arguments = ARGUMENT_PARSER.parse(context, argumentsSource);
            ClassPrototype[] argumentTypes = ExpressionUtils.toTypes(arguments);
            PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName.getValue(), argumentTypes);

            if (prototypeMethod == null) {
                throw PandaParserFailure.builder("Class " + prototype.getClassName() + " does not have method '" + methodName + "' with these parameters", context)
                        .withStreamOrigin(argumentsSource)
                        .withNote("Change arguments or add a new method with the provided types of parameters")
                        .build();
            }

            if (!prototypeMethod.isStatic() && instance instanceof StaticExpression) {
                throw PandaParserFailure.builder("Cannot invoke non-static method on static context", context)
                        .withStreamOrigin(methodName)
                        .withNote("Call method using class instance or add missing 'static' keyword to the '" + methodName.getValue() + "'method signature")
                        .build();
            }

            MethodInvoker invoker =  new MethodInvoker(prototypeMethod, instance, arguments);
            return new MethodInvokerExpressionCallback(invoker);
        }

    }

}
