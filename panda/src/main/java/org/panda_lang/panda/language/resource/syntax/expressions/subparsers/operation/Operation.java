/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern.OperationPatternElement;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern.OperationPatternResult;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;

import java.util.ArrayList;
import java.util.List;

public final class Operation {

    private final List<OperationElement> elements;

    public Operation(List<OperationElement> elements) {
        if (elements.size() < 3 || (elements.size() % 2) == 0) {
            throw new IllegalArgumentException("Invalid number of elements: " + elements.size());
        }

        this.elements = elements;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "elements=" + elements +
                '}';
    }

    public List<OperationElement> getElements() {
        return elements;
    }

    private static OperationElement asOperatorElement(ExpressionParser parser, Context context, ExpressionContext expressionContext, OperationPatternElement element) {
        if (element.isOperator()) {
            return new OperationElement(element.getOperator());
        }

        Snippet source = element.getExpression();
        ExpressionTransaction transaction = parser.parse(context, source);
        expressionContext.commit(transaction::rollback);

        return new OperationElement(transaction.getExpression());
    }

    public static Operation of(ExpressionParser parser, Context context, ExpressionContext expressionContext, OperationPatternResult result) {
        List<OperationElement> elements = new ArrayList<>(result.size());

        for (OperationPatternElement element : result.getElements()) {
            elements.add(asOperatorElement(parser, context, expressionContext, element));
        }

        return new Operation(elements);
    }

    public static final class OperationElement {

        private final Expression expression;
        private final TokenInfo operator;

        public OperationElement(Expression expression) {
            this.expression = expression;
            this.operator = null;
        }

        public OperationElement(TokenInfo operator) {
            this.operator = operator;
            this.expression = null;
        }

        public boolean isExpression() {
            return expression != null;
        }

        public boolean isOperator() {
            return operator != null;
        }

        public TokenInfo getOperatorRepresentation() {
            return operator;
        }

        public @Nullable Operator getOperator() {
            return isOperator() ? (Operator) getOperatorRepresentation().getToken() : null;
        }

        public Expression getExpression() {
            return expression;
        }

        @Override
        public String toString() {
            return "OperationElement{" +
                    "expression=" + expression +
                    ", operator=" + operator +
                    '}';
        }
    }

}
