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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.method.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.architecture.dynamic.StandaloneExecutable;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractExecutableStatement;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;

public class MethodInvoker extends AbstractExecutableStatement implements StandaloneExecutable {

    private final PrototypeMethod method;
    private final Expression instanceExpression;
    private final Expression[] arguments;

    public MethodInvoker(PrototypeMethod method, @Nullable Expression instance, Expression[] arguments) {
        if (method == null) {
            throw new IllegalArgumentException("PrototypeMethod cannot be null");
        }

        this.method = method;
        this.instanceExpression = instance;
        this.arguments = arguments;
    }

    @Override
    public void execute(Frame frame) {
        Value instance = null;

        if (instanceExpression != null) {
            instance = instanceExpression.evaluate(frame);

            // exclude static and null values
            if (instance != null && !instance.isNull()) {
                frame.instance(instance);
            }
        }

        Value[] values = ExpressionUtils.getValues(frame, arguments);

        try {
            method.invoke(frame, instance != null ? instance.getObject() : null, values);
        } catch (Exception e) {
            throw new PandaFrameworkException("Internal error: " + e.getMessage(), e);
        }
    }

    public PrototypeMethod getMethod() {
        return method;
    }

}
