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
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserPostProcessor;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationParser;

import java.util.ArrayList;
import java.util.List;

public final class OperationExpressionSubparser implements ExpressionSubparser {

    private static final OperationParser OPERATION_PARSER = new OperationParser();

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new OperationWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.MUTUAL;
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public String getSubparserName() {
        return "operation";
    }

    private static final class OperationWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserPostProcessor {

        private List<Operation.OperationElement> elements;

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            if (!context.hasResults()) {
                return null;
            }

            if (token.getType() != TokenTypes.OPERATOR) {/*
                if (elements != null) {
                    elements.add(new Operation.OperationElement(context.popExpression()));
                    return finish(context);
                }
                */
                return null;
            }

            if (elements == null) {
                this.elements = new ArrayList<>(3);
            }

            elements.add(new Operation.OperationElement(context.popExpression()));
            elements.add(new Operation.OperationElement(token));

            return ExpressionResult.empty();
        }

        @Override
        public @Nullable ExpressionResult finish(ExpressionContext context) {
            if (elements == null) {
                return null;
            }

            if (context.hasResults()) {
                elements.add(new Operation.OperationElement(context.popExpression()));
            }

            if (elements.size() < 3 || (elements.size() % 2) == 0) {
                context.getResults().clear();
                return ExpressionResult.error("Cannot parse operation: " + null, context.getSynchronizedSource().getCurrent());
            }

            Operation operation = new Operation(elements);
            this.elements = null;

            return ExpressionResult.of(OPERATION_PARSER.parse(context.getContext(), operation));
        }

    }

}
