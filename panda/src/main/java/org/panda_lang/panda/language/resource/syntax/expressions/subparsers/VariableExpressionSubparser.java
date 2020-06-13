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
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.architecture.expression.ThisExpression;
import org.panda_lang.framework.language.architecture.type.TypeComponents;
import org.panda_lang.framework.language.architecture.type.utils.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.number.NumberUtils;
import org.panda_lang.utilities.commons.function.Option;

public final class VariableExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new VariableWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "variable";
    }

    private static final class VariableWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenInfo token) {
            boolean period = TokenUtils.contentEquals(context.getSynchronizedSource().getPrevious(), Separators.PERIOD);

            if (token.getType() != TokenTypes.UNKNOWN) {
                return null;
            }

            if ((!period && context.hasResults()) || (period && !context.hasResults())) {
                return null;
            }

            // do not accept variable names starting with numbers
            if (NumberUtils.startsWithNumber(token.getToken())) {
                return null;
            }

            String name = token.getValue();

            // if there is anything on stack, we can search only for fields
            if (context.hasResults()) {
                ExpressionResult result = fromInstance(context, context.peekExpression(), token).orElseGet(() -> {
                    return ExpressionResult.error("Cannot find field called '" + name + "'", token);
                });

                if (result.isPresent()) {
                    context.popExpression();
                }

                return result;
            }

            Option<Variable> variableValue = context.getContext().getComponent(Components.SCOPE).getVariable(name);

            // respect local variables before fields
            if (variableValue.isDefined()) {
                Variable variable = variableValue.get();
                return ExpressionResult.of(new VariableExpression(variable).toExpression());
            }

            Type type = context.getContext().getComponent(TypeComponents.PROTOTYPE);

            if (type != null) {
                return fromInstance(context, ThisExpression.of(type), token).orElseGet(() -> {
                    return ExpressionResult.error("Cannot find class/variable '" + name + "'", token);
                });
            }

            // return ExpressionResult.error("Cannot find variable or field called '" + name + "'", token);
            return null;
        }

        private Option<ExpressionResult> fromInstance(ExpressionContext context, Expression instance, TokenInfo name) {
            Option<TypeField> fieldValue = instance.getType().getFields().getField(name.getValue());

            if (fieldValue.isDefined()) {
                Option<String> issue = VisibilityComparator.canAccess(fieldValue.get(), context.getContext());

                if (issue.isDefined()) {
                    throw new PandaExpressionParserFailure(context, name, issue.get(), VisibilityComparator.NOTE_MESSAGE);
                }
            }

            return fieldValue.map(property -> ExpressionResult.of(new FieldExpression(instance, property).toExpression()));
        }

    }

}
