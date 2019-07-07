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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.ParameterizedExecutable;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionType;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;

public final class ParametrizedExpression implements Expression {

    private final ParameterizedExecutable executable;
    private final Expression[] arguments;

    public ParametrizedExpression(ParameterizedExecutable executable, Expression[] arguments) {
        this.executable = executable;
        this.arguments = arguments;
    }

    @Override
    public Value evaluate(Frame frame) {
        Value[] values = ExpressionUtils.getValues(frame, arguments);
        Value instance = frame.getInstance();

        try {
            return executable.invoke(frame, instance != null ? instance.getObject() : null, values);
        } catch (Exception e) {
            throw new PandaFrameworkException("Internal error: " + e.getMessage(), e);
        }
    }

    @Override
    public ClassPrototype getReturnType() {
        return executable.getReturnType().fetch();
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.UNKNOWN;
    }

}
