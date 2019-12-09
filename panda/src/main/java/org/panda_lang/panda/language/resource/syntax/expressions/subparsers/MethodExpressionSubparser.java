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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Adjustment;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.architecture.expression.StaticExpression;
import org.panda_lang.framework.language.architecture.expression.ThisExpression;
import org.panda_lang.framework.language.architecture.prototype.PrototypeExecutableExpression;
import org.panda_lang.framework.language.architecture.prototype.utils.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.SynchronizedSource;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.Arrays;
import java.util.Optional;

public final class MethodExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new MethodWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "method";
    }

    private static final class MethodWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation nameToken) {
            SynchronizedSource source = context.getSynchronizedSource();

            // name has to be declared by unknown type of token
            if (nameToken.getType() != TokenTypes.UNKNOWN || !source.hasNext()) {
                return null;
            }

            // section of arguments
            @Nullable Section section = ObjectUtils.cast(Section.class, source.next().getToken());

            // section type required
            if (section == null || !section.getSeparator().equals(Separators.PARENTHESIS_LEFT)) {
                return null;
            }

            // fetch method instance
            Expression instance = null;

            // fetch instance from stack if token before name was period
            if (context.hasResults() && TokenUtils.contentEquals(source.getPrevious(1), Separators.PERIOD)) {
                instance = context.peekExpression();
            }
            // use current instance (this) if source contains only name and section
            else /* if (source.getIndex() == 2) ^ not really */ {
                instance = ThisExpression.of(context.getContext());
            }

            // instance required
            if (instance == null) {
                return null;
            }

            // check if prototype of instance contains required method
            if (!instance.getReturnType().getMethods().hasPropertyLike(nameToken.getValue())) {
                return ExpressionResult.error("Cannot find method called '" + nameToken.getValue() + "'", nameToken);
            }

            // parse method
            Expression expression = parseMethod(context, instance, nameToken, section.getContent());

            // drop used instance
            if (context.hasResults()) {
                context.popExpression();
            }

            return ExpressionResult.of(expression);
        }

        private Expression parseMethod(ExpressionContext context, Expression instance, TokenRepresentation methodName, Snippet argumentsSource) {
            Expression[] arguments = ARGUMENT_PARSER.parse(context, argumentsSource);
            Optional<Adjustment<PrototypeMethod>> adjustedArguments = instance.getReturnType().getMethods().getAdjustedArguments(methodName.getValue(), arguments);

            if (!adjustedArguments.isPresent()) {
                String types = ContentJoiner.on(", ").join(Arrays.stream(arguments)
                        .map(Expression::getReturnType)
                        .map(Prototype::getSimpleName)
                        .toArray()
                ).toString();

                throw new PandaExpressionParserFailure(context, argumentsSource,
                        "Class " + instance.getReturnType().getSimpleName() + " does not have method &1" + methodName + "&r with parameters &1" + types + "&r",
                        "Change arguments or add a new method with the provided types of parameters"
                );
            }

            PrototypeMethod method = adjustedArguments.get().getExecutable();

            if (!method.isStatic() && instance instanceof StaticExpression) {
                throw new PandaExpressionParserFailure(context, methodName,
                        "Cannot invoke non-static method on static context",
                        "Call method using class instance or add missing 'static' keyword to the '" + methodName.getValue() + "'method signature"
                );
            }

            Optional<String> issue = VisibilityComparator.canAccess(method, context.getContext());

            if (issue.isPresent()) {
                throw new PandaExpressionParserFailure(context, methodName, issue.get(), VisibilityComparator.NOTE_MESSAGE);
            }

            return new PrototypeExecutableExpression(instance, adjustedArguments.get());
        }

    }

}
