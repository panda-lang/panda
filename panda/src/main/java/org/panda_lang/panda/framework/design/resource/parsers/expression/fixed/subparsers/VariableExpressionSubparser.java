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
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionResult;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.FieldExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.VariableExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.Stack;

public class VariableExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createSubparser() {
        return new VariableWorker();
    }

    static class VariableWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult<Expression> next(ExpressionParser parser, ParserData data, TokenRepresentation token, Stack<Expression> results) {
            if (Separators.PERIOD.equals(token.getToken())) {
                return results.isEmpty() ? null : ExpressionResult.empty();
            }

            if (token.getType() != TokenType.UNKNOWN) {
                return null;
            }

            ScopeLinker scopeLinker = data.getComponent(UniversalComponents.SCOPE_LINKER);
            Scope scope = scopeLinker.getCurrentScope();

            String name = token.getTokenValue();
            Variable variable = scope.getVariable(name);

            if (variable != null) {
                return ExpressionResult.of(new VariableExpressionCallback(variable, scope.indexOf(variable)).toExpression());
            }

            ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

            if (prototype != null) {
                PrototypeField field = prototype.getFields().getField(token.getTokenValue());

                if (field != null) {
                    int memoryIndex = prototype.getFields().getIndexOfField(field);
                    return ExpressionResult.of(new FieldExpressionCallback(ThisExpressionCallback.asExpression(prototype), field, memoryIndex).toExpression());
                }
            }

            return ExpressionResult.error("Cannot find variable or field called '" + name + "'", token);
        }

        @Override
        public boolean isDone() {
            return false;
        }

    }

}
