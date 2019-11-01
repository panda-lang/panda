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

package org.panda_lang.framework.design.architecture.expression;

import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

public final class ExpressionUtils {

    private ExpressionUtils() { }

    /**
     * Evaluates static expression (constexpr) - expression that does not relay on stack and instance
     *
     * @param expression the expression to evaluate
     * @param <T> generic type of result
     * @return expression value
     */
    public static <T> T evaluateConstExpression(Expression expression) {
        try {
            return expression.evaluate(null, null);
        } catch (Exception e) {
            throw new PandaRuntimeException("Cannot evaluate static expression: " + expression);
        }
    }

    /**
     * Evaluates the given expressions and returns values stored in the array in the preserved order
     *
     * @param stack the stack to use
     * @param instance the instance to use
     * @param expressions the expressions to evaluate
     * @return array of values
     * @throws Exception if something happen
     */
    public static Object[] evaluate(ProcessStack stack, Object instance, Expression... expressions) throws Exception {
        Object[] values = new Object[expressions.length];

        for (int index = 0; index < values.length; index++) {
            values[index] = expressions[index].evaluate(stack, instance);
        }

        return values;
    }

}
