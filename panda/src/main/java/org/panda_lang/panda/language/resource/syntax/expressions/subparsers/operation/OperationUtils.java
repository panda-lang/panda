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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation;

import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.function.Predicate;

final class OperationUtils {

    static boolean isConcatenation(Operation operation) {
        boolean operator = false;
        boolean string = false;

        for (Operation.OperationElement element : operation.getElements()) {
            if (!operator && element.isOperator()) {
                operator = Operators.ADDITION.equals(element.getOperator());
            }
            else if (!string && element.isExpression()) {
                string = String.class.isAssignableFrom(element.getExpression().getType().getAssociatedClass());
            }

            if (operator && string) {
                break;
            }
        }

        return operator && string;
    }

    static boolean isNumeric(Operation operation) {
        return verify(operation, null, element -> {
            if (element.isExpression()) {
                return Number.class.isAssignableFrom(element.getExpression().getType().getAssociatedClass());
            }

            Operator operator = ObjectUtils.cast(Operator.class, element.getOperatorRepresentation().getToken());

            if (operator == null) {
                throw new PandaParserException("Token is not an operator");
            }

            return OperatorUtils.isMemberOf(operator, OperatorFamilies.MATH);
        });
    }

    static boolean isLogical(Operation operation) {
        return verify(operation, Type.OPERATOR, element -> OperatorUtils.isMemberOf(element.getOperator(), OperatorFamilies.LOGICAL));
    }

    static boolean verifyOperator(Operation operation, Token token) {
        return verify(operation, Type.OPERATOR, element -> element.getOperatorRepresentation().contentEquals(token));
    }

    static boolean verify(Operation operation, Type type, Predicate<Operation.OperationElement> filter) {
        for (Operation.OperationElement element : operation.getElements()) {
            if (type == Type.EXPRESSION && element.isOperator()) {
                continue;
            }

            if (type == Type.OPERATOR && element.isExpression()) {
                continue;
            }

            if (!filter.test(element)) {
                return false;
            }
        }

        return true;
    }

    private enum Type {

        EXPRESSION,
        OPERATOR

    }

}
