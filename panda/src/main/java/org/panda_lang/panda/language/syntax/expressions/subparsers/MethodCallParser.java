/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.expression.StaticExpression;
import org.panda_lang.framework.architecture.expression.ThisExpression;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.TypedUtils;
import org.panda_lang.framework.architecture.type.VisibilityComparator;
import org.panda_lang.framework.architecture.type.member.method.TypeMethod;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.architecture.type.signature.AdjustedExpression;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.SynchronizedSource;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.interpreter.token.TokenUtils;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.auxiliary.Section;
import org.panda_lang.framework.resource.syntax.separator.Separators;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.console.Effect;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.text.Joiner;

import java.util.List;

public final class MethodCallParser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new MethodWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String name() {
        return "method";
    }

    private static final class MethodWorker extends AbstractExpressionSubparserWorker {

        private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo nameToken) {
            SynchronizedSource source = context.getSynchronizedSource();

            // name has to be declared by unknown or sequence token
            if ((nameToken.getType() != TokenTypes.UNKNOWN) && (nameToken.getType() != TokenTypes.SEQUENCE) || !source.hasNext()) {
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
            boolean autofilled = false;

            // fetch instance from stack if token before name was period
            boolean hasPeriod = TokenUtils.contentEquals(source.getPrevious(1), Separators.PERIOD);

            if (context.hasResults() && hasPeriod) {
                instance = context.peekExpression();
            }
            // use current instance (this) if source contains only name and section
            else if (!hasPeriod) /* if (source.getIndex() == 2) ^ not really */ {
                instance = ThisExpression.ofUnknownContext(context.toContext());
                autofilled = true;
            }

            // instance required
            if (instance == null) {
                return null;
            }

            Type type = instance.getSignature().toTyped().fetchType();

            // check if type of instance contains required method
            if (!type.getMethods().hasPropertyLike(nameToken.getValue())) {
                return ExpressionResult.error("Cannot find method called '" + nameToken.getValue() + "' in type " + type, nameToken);
            }

            // parse method
            Expression expression = parseMethod(context, type, instance, nameToken, section.getContent());

            // drop used instance
            if (context.hasResults() && !autofilled) {
                context.popExpression();
            }

            return ExpressionResult.of(expression);
        }

        private Expression parseMethod(ExpressionContext<?> context, Type type, Expression instance, TokenInfo methodName, Snippet argumentsSource) {
            List<Expression> arguments = ARGUMENT_PARSER.parse(context, argumentsSource);
            Option<TypeMethod> matchedMethod = type.getMethods().getMethod(methodName.getValue(), arguments);

            if (!matchedMethod.isDefined()) {
                String types = TypedUtils.toString(arguments);

                List<? extends TypeMethod> propertiesLike = type.getMethods().getPropertiesLike(methodName.getValue());
                String similar = "";

                if (!propertiesLike.isEmpty()) {
                    similar = "Similar methods:&r" + Effect.LINE_SEPARATOR;
                    similar += Joiner.on(Effect.LINE_SEPARATOR.toString()).join(propertiesLike, method -> {
                        return "  • &7" + method.getName() + "&r";
                    });
                }

                throw new PandaParserFailure(context.toContext(), argumentsSource,
                        "Class &r" + type.getSimpleName() + "&1 does not have method &r" + methodName + "&1 with parameters &r" + types,
                        "Change arguments or add a new method with the provided types of parameters. " + similar
                );
            }

            TypeMethod method = matchedMethod.get();

            for (int index = 0; index < arguments.size(); index++) {
                PropertyParameter parameter = method.getParameters().get(index);
                Expression argument = arguments.get(index);

                if (!parameter.getSignature().apply(instance).isAssignableFrom(argument)) {
                    throw new PandaParserFailure(context.toContext(), methodName, "Parameter " + parameter + " does not accept " + argument.getSignature() + " type");
                }
            }

            if (!method.isStatic() && instance instanceof StaticExpression) {
                throw new PandaParserFailure(context.toContext(), methodName,
                        "Cannot invoke non-static method on static context",
                        "Call method using class instance or add missing 'static' keyword to the '" + methodName.getValue() + "'method signature"
                );
            }

            Option<String> issue = VisibilityComparator.canAccess(method, context.toContext());

            if (issue.isDefined()) {
                throw new PandaParserFailure(context.toContext(), methodName, issue.get(), VisibilityComparator.NOTE_MESSAGE);
            }

            return new AdjustedExpression(instance, matchedMethod.get(), arguments);
        }

    }

}
