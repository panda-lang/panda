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

package org.panda_lang.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.architecture.expression.ExpressionValueType;
import org.panda_lang.framework.design.architecture.prototype.Adjustment;
import org.panda_lang.framework.design.architecture.prototype.ExecutableProperty;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.ProcessStack;

public final class PrototypeExecutableExpression implements Expression {

    private final ExecutableProperty executable;
    private final Expression instanceExpression;
    private final Expression[] arguments;

    public PrototypeExecutableExpression(@Nullable Expression instance, Adjustment<?> adjustment) {
        this(instance, adjustment.getExecutable(), adjustment.getArguments());
    }

    public PrototypeExecutableExpression(@Nullable Expression instance, ExecutableProperty executable, Expression[] arguments) {
        this.executable = executable;
        this.instanceExpression = instance;
        this.arguments = arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        Object[] values = ExpressionUtils.evaluate(stack, instance, arguments);

        if (instanceExpression != null) {
            instance = instanceExpression.evaluate(stack, instance);
        }

        return executable.invoke(stack, instance, values);
    }

    @Override
    public Prototype getType() {
        return executable.getType();
    }

    @Override
    public ExpressionValueType getExpressionType() {
        return ExpressionValueType.DYNAMIC;
    }

    @Override
    public String toString() {
        return executable + " -> " + getType().getSimpleName();
    }

}
