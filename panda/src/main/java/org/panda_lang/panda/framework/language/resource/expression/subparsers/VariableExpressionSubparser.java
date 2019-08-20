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

package org.panda_lang.panda.framework.language.resource.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.expression.subparsers.number.NumberUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.ThisExpression;

import java.util.Optional;

public class VariableExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new VariableWorker().withSubparser(this);
    }

    @Override
    public String getSubparserName() {
        return "variable";
    }

    private static class VariableWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            boolean period = TokenUtils.contentEquals(context.getDiffusedSource().getPrevious(), Separators.PERIOD);

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            if ((!period && context.hasResults()) || (period && !context.hasResults())) {
                return null;
            }

            if (NumberUtils.startsWithNumber(token.getToken())) {
                return null;
            }

            ScopeLinker scopeLinker = context.getContext().getComponent(UniversalComponents.LINKER);
            Scope scope = scopeLinker.getCurrentScope();

            String name = token.getValue();
            Variable variable = scope.getVariable(name);

            if (variable != null) {
                return ExpressionResult.of(new VariableExpression(variable, scope.indexOf(variable)).toExpression());
            }

            if (context.hasResults()) {
                ExpressionResult result = fromInstance(context.peekExpression(), name)
                        .orElseGet(() -> ExpressionResult.error("Cannot find field called '" + name + "'", token));

                if (result.isPresent()) {
                    context.popExpression();
                }

                return result;
            }

            ClassPrototype prototype = context.getContext().getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

            if (prototype != null) {
                return fromInstance(ThisExpression.of(prototype), name).orElseGet(() -> ExpressionResult.error("Cannot find class/variable '" + name + "'", token));
            }

            // return ExpressionResult.error("Cannot find variable or field called '" + name + "'", token);
            return null;
        }

        private Optional<ExpressionResult> fromInstance(Expression instance, String name) {
            ClassPrototype prototype = instance.getReturnType();
            PrototypeField field = prototype.getFields().getField(name);

            if (field != null) {
                return Optional.of(ExpressionResult.of(new FieldExpression(instance, field).toExpression()));
            }

            return Optional.empty();
        }

    }

}
