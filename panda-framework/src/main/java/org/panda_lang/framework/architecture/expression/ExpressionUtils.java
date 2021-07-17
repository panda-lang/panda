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

package org.panda_lang.framework.architecture.expression;

import org.panda_lang.framework.architecture.type.Autocast;
import org.panda_lang.framework.architecture.type.AutocastDynamicExpression;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.runtime.PandaProcess;
import org.panda_lang.framework.runtime.PandaProcessStack;
import org.panda_lang.framework.runtime.PandaRuntimeConstants;
import org.panda_lang.framework.runtime.PandaRuntimeException;
import org.panda_lang.framework.runtime.Process;
import org.panda_lang.framework.runtime.ProcessStack;
import panda.std.Option;
import panda.std.Result;

import java.util.List;

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
        } catch (Exception exception) {
            throw new PandaRuntimeException("Cannot evaluate static expression: " + expression, exception);
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
    public static Object[] evaluate(ProcessStack stack, Object instance, List<? extends Expression> expressions) throws Exception {
        Object[] values = new Object[expressions.size()];

        for (int index = 0; index < values.length; index++) {
            values[index] = expressions.get(index).evaluate(stack, instance);
        }

        return values;
    }

    /**
     * Prepare expression to be in the same type as the requested type (supports autocasts)
     *
     * @param expression the expression to equalize
     * @param expected the target type
     * @return expression in the given type
     */
    public static Result<Expression, String> equalize(Expression expression, Signature expected) {
        if (expression.isNull()) {
            return Result.ok(expression);
        }

        if (expected.isAssignableFrom(expression.getSignature())) {
            return Result.ok(expression);
        }

        if (!expression.getSignature().isTyped()) {
            return Result.error("Cannot equalize generic expression");
        }

        if (!expected.isTyped()) {
            return Result.error("Cannot equalize to generic signature");
        }

        Option<? extends Autocast<?, ?>> autocast = expression.getSignature().toTyped().fetchType().getAutocast(expected.toTyped().getReference());

        if (autocast.isPresent()) {
            return Result.ok(new AutocastDynamicExpression(expression, expected, autocast.get()).toExpression());
        }

        return Result.error("Cannot find associated autocast");
    }

}
