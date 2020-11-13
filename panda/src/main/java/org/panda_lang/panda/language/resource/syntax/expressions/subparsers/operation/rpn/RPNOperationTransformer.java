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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;

import java.util.Map;
import java.util.Stack;

final class RPNOperationTransformer {

    private final Map<Operator, Integer> priorities;
    private final Map<Operator, RPNOperationSupplier<?>> suppliers;

    RPNOperationTransformer(RPNOperationBuilder builder) {
        this.priorities = builder.priorities;
        this.suppliers = builder.suppliers;
    }

    public RPNOperation parse(Context<?> context, Operation operation) {
        Stack<Object> values = new Stack<>();
        Stack<Operator> operators = new Stack<>();

        for (Operation.OperationElement element : operation.getElements()) {
            if (element.isExpression()) {
                values.push(element.getExpression());
                continue;
            }

            Operator operator = element.getOperator();

            if (!priorities.containsKey(operator)) {
                throw new PandaParserFailure(context, element.getOperatorRepresentation(), "Unexpected or unsupported operator " + operator);
            }

            if (operators.size() != 0 && compare(operators.peek(), operator)) {
                values.push(operators.pop());
            }

            operators.push(operator);
        }

        while (!operators.isEmpty()) {
            values.push(operators.pop());
        }

        return new RPNOperation(context.getTypeLoader(), suppliers, values);
    }

    private boolean compare(Operator a, Operator b) {
        return getPriority(a) >= getPriority(b);
    }

    private int getPriority(Operator operator) {
        return priorities.get(operator);
    }

}
