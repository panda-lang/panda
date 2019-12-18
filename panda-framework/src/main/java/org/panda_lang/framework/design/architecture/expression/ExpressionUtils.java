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

import org.panda_lang.framework.design.architecture.prototype.Autocast;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.prototype.AutocastDynamicExpression;
import org.panda_lang.framework.language.runtime.PandaProcess;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.framework.language.runtime.PandaRuntimeConstants;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.Optional;

public final class ExpressionUtils {

    private static final Process PROCESS = new PandaProcess(null, null);

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
            return expression.evaluate(new PandaProcessStack(PROCESS, PandaRuntimeConstants.DEFAULT_STACK_SIZE), null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PandaRuntimeException("Cannot evaluate static expression: " + expression, e);
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

    /**
     * Prepare expression to be in the same type as the requested type (supports autocasts)
     *
     * @param expression the expression to equalize
     * @param target the target type
     * @return expression in the given type
     */
    public static Expression equalize(Expression expression, Prototype target) {
        if (expression.isNull()) {
            return expression;
        }

        Optional<Autocast<?, ?>> autocast = expression.getType().getAutocast(target);

        if (autocast.isPresent()) {
            return new AutocastDynamicExpression(expression, target, autocast.get()).toExpression();
        }

        return expression;
    }

}
