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

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

import java.util.function.Predicate;

public class OperationUtils {

    public static boolean verifyOperator(Operation operation, Token token) {
        return verify(operation, Type.OPERATOR, element -> element.getOperator().contentEquals(token));
    }

    public static boolean isNumeric(Operation operation) {
        return verify(operation, Type.EXPRESSION, element -> PandaTypes.NUMBER.isAssignableFrom(element.getExpression().getReturnType()));
    }

    private static boolean verify(Operation operation, Type type, Predicate<Operation.OperationElement> filter) {
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
