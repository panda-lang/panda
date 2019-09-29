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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.architecture.expression.ThisExpression;
import org.panda_lang.framework.language.architecture.prototype.PrototypeComponents;
import org.panda_lang.framework.language.architecture.prototype.VisibilityComparator;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.resource.expression.subparsers.number.NumberUtils;

import java.util.Optional;

public final class VariableExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new VariableWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "variable";
    }

    private static final class VariableWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            boolean period = TokenUtils.contentEquals(context.getSynchronizedSource().getPrevious(), Separators.PERIOD);

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            if ((!period && context.hasResults()) || (period && !context.hasResults())) {
                return null;
            }

            if (NumberUtils.startsWithNumber(token.getToken())) {
                return null;
            }

            String name = token.getValue();
            Optional<Variable> variableValue = context.getContext().getComponent(Components.SCOPE).getVariable(name);

            if (variableValue.isPresent()) {
                Variable variable = variableValue.get();
                return ExpressionResult.of(new VariableExpression(variable).toExpression());
            }

            if (context.hasResults()) {
                ExpressionResult result = fromInstance(context, context.peekExpression(), token)
                        .orElseGet(() -> ExpressionResult.error("Cannot find field called '" + name + "'", token));

                if (result.isPresent()) {
                    context.popExpression();
                }

                return result;
            }

            Prototype prototype = context.getContext().getComponent(PrototypeComponents.PROTOTYPE);

            if (prototype != null) {
                return fromInstance(context, ThisExpression.of(prototype), token).orElseGet(() -> {
                    return ExpressionResult.error("Cannot find class/variable '" + name + "'", token);
                });
            }

            // return ExpressionResult.error("Cannot find variable or field called '" + name + "'", token);
            return null;
        }

        private Optional<ExpressionResult> fromInstance(ExpressionContext context, Expression instance, TokenRepresentation name) {
            Prototype prototype = instance.getReturnType();
            PrototypeField field = prototype.getFields().getField(name.getValue());

            if (field != null) {
                Optional<String> issue = VisibilityComparator.canAccess(field, context.getContext());

                if (issue.isPresent()) {
                    throw new PandaExpressionParserFailure(context, name, issue.get(), VisibilityComparator.NOTE_MESSAGE);
                }
            }

            return Optional.ofNullable(field).map(property -> ExpressionResult.of(new FieldExpression(instance, field).toExpression()));
        }

    }

}
