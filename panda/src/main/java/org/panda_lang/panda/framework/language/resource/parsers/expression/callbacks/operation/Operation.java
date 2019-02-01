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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePatternElement;
import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePatternResult;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.resource.parsers.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;

import java.util.ArrayList;
import java.util.List;

public class Operation {

    private final List<OperationElement> elements;

    public Operation(List<OperationElement> elements) {
        if (elements.size() < 3 || (elements.size() % 2) == 0) {
            throw new IllegalArgumentException("Invalid number of elements: " + elements.size());
        }

        this.elements = elements;
    }

    public List<OperationElement> getElements() {
        return elements;
    }

    private static OperationElement asOperatorElement(ExpressionParser parser, ParserData data, ProgressivePatternElement element) {
        if (element.isOperator()) {
            return new OperationElement(element.getOperator());
        }

        Tokens source = element.getExpression();
        Expression expression = parser.parse(data, source);

        return new OperationElement(expression);
    }

    public static Operation of(ExpressionParser parser, ParserData data, ProgressivePatternResult result) {
        List<OperationElement> elements = new ArrayList<>(result.size());

        for (ProgressivePatternElement element : result.getElements()) {
            elements.add(asOperatorElement(parser, data, element));
        }

        return new Operation(elements);
    }

    public static class OperationElement {

        private final Expression expression;
        private final TokenRepresentation operator;

        public OperationElement(Expression expression) {
            this.expression = expression;
            this.operator = null;
        }

        public OperationElement(TokenRepresentation operator) {
            this.operator = operator;
            this.expression = null;
        }

        public boolean isExpression() {
            return expression != null;
        }

        public boolean isOperator() {
            return operator != null;
        }

        public TokenRepresentation getOperatorRepresentation() {
            return operator;
        }

        public Operator getOperator() {
            return (Operator) getOperatorRepresentation().getToken();
        }

        public Expression getExpression() {
            return expression;
        }

    }

}
