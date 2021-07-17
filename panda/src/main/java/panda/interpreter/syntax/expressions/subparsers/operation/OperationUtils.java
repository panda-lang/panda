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

package panda.interpreter.syntax.expressions.subparsers.operation;

import panda.interpreter.architecture.type.Type;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.token.Token;
import panda.interpreter.resource.syntax.operator.Operator;
import panda.interpreter.resource.syntax.operator.OperatorFamilies;
import panda.interpreter.resource.syntax.operator.OperatorUtils;
import panda.interpreter.resource.syntax.operator.Operators;
import panda.utilities.ObjectUtils;

import java.util.function.Predicate;

final class OperationUtils {

    private enum OperationType {
        EXPRESSION,
        OPERATOR
    }

    static boolean isNumeric(Operation operation) {
        return verify(operation, null, element -> {
            if (element.isExpression()) {
                Type expressionType = element.getExpression().getKnownType();
                return expressionType.getTypeLoader().get().requireType("panda/panda@::Number").isAssignableFrom(expressionType);
            }

            Operator operator = ObjectUtils.cast(Operator.class, element.getOperatorRepresentation().getToken());

            if (operator == null) {
                throw new PandaParserException("Token is not an operator");
            }

            return OperatorUtils.isMemberOf(operator, OperatorFamilies.MATH);
        });
    }

    static boolean isConcatenation(Operation operation) {
        boolean operator = false;
        boolean string = false;

        for (Operation.OperationElement element : operation.getElements()) {
            if (!operator && element.isOperator()) {
                operator = Operators.ADDITION.equals(element.getOperator());
            }
            else if (!string && element.isExpression()) {
                string = element.getExpression().getKnownType().is("panda/panda@::String");
            }

            if (operator && string) {
                break;
            }
        }

        return operator && string;
    }

    static boolean isLogical(Operation operation) {
        return verify(operation, OperationType.OPERATOR, element -> OperatorUtils.isMemberOf(element.getOperator(), OperatorFamilies.LOGICAL));
    }

    static boolean isBitwise(Operation operation) {
        return verify(operation, OperationType.OPERATOR, element -> OperatorUtils.isMemberOf(element.getOperator(), OperatorFamilies.BITWISE));
    }

    static boolean verifyOperator(Operation operation, Token token) {
        return verify(operation, OperationType.OPERATOR, element -> element.getOperatorRepresentation().contentEquals(token));
    }

    static boolean verify(Operation operation, OperationType type, Predicate<Operation.OperationElement> filter) {
        for (Operation.OperationElement element : operation.getElements()) {
            if (type == OperationType.EXPRESSION && element.isOperator()) {
                continue;
            }

            if (type == OperationType.OPERATOR && element.isExpression()) {
                continue;
            }

            if (!filter.test(element)) {
                return false;
            }
        }

        return true;
    }

}
