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
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.parameter.Arguments;
import org.panda_lang.framework.design.architecture.prototype.PrototypeExecutable;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionValueType;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;

public final class PrototypeExecutableExpression implements Expression {

    private final PrototypeExecutable executable;
    private final Expression instanceExpression;
    private final Expression[] arguments;

    public PrototypeExecutableExpression(@Nullable Expression instance, Arguments<?> arguments) {
        this(instance, arguments.getExecutable(), arguments.getArguments());
    }

    public PrototypeExecutableExpression(@Nullable Expression instance, PrototypeExecutable executable, Expression[] arguments) {
        this.executable = executable;
        this.instanceExpression = instance;
        this.arguments = arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) {
        Object[] values = ExpressionUtils.getValues(stack, instance, arguments);

        if (instanceExpression != null) {
            instance = instanceExpression.evaluate(stack, instance);
        }

        try {
            return executable.invoke(stack, instance, values);
        } catch (Exception e) {
            throw new PandaFrameworkException("Internal error: " + e.getMessage(), e);
        }
    }

    @Override
    public Prototype getReturnType() {
        return executable.getReturnType().fetch();
    }

    @Override
    public ExpressionValueType getType() {
        return ExpressionValueType.UNKNOWN;
    }

}
