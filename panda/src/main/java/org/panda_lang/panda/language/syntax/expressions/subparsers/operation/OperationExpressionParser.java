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

package org.panda_lang.panda.language.syntax.expressions.subparsers.operation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.interpreter.parser.Context;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserPostProcessor;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.framework.interpreter.token.TokenInfo;
import org.panda_lang.framework.resource.syntax.TokenTypes;
import org.panda_lang.framework.resource.syntax.operator.Operator;
import org.panda_lang.framework.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.Operation.OperationElement;

import java.util.ArrayList;
import java.util.List;

public final class OperationExpressionParser implements ExpressionSubparser {

    private static final OperationParser OPERATION_PARSER = new OperationParser();

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new OperationWorker().withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.MUTUAL;
    }

    @Override
    public String name() {
        return "operation";
    }

    private static final class OperationWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserPostProcessor {

        private List<Operation.OperationElement> elements;

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (token.getType() != TokenTypes.OPERATOR) {/*
                if (elements != null) {
                    elements.add(new Operation.OperationElement(context.popExpression()));
                    return finish(context);
                }
                */
                return null;
            }

            Operator operator = token.toToken();

            if (!operator.isConjunction()) {
                return null;
            }

            if (elements == null) {
                this.elements = new ArrayList<>(3);
            }

            elements.add(new OperationElement(context.popExpression()));
            elements.add(new OperationElement(token));

            return ExpressionResult.empty();
        }

        @Override
        public @Nullable ExpressionResult finish(ExpressionContext<?> context) {
            if (elements == null) {
                return null;
            }

            if (context.hasResults()) {
                elements.add(new Operation.OperationElement(context.popExpression()));
            }

            if (elements.size() < 3 || (elements.size() % 2) == 0) {
                context.getResults().clear();

                return ExpressionResult.error("Cannot parse operation: " + null, context.getSynchronizedSource().getCurrent()
                        .map(token -> (Snippetable) token)
                        .orElseGet(context.getSynchronizedSource().getSource()));
            }

            Operation operation = new Operation(elements);
            this.elements = null;

            return OPERATION_PARSER.parse(context.toContext(), operation)
                    .map(ExpressionResult::of)
                    .getOrNull();
        }

    }

}
