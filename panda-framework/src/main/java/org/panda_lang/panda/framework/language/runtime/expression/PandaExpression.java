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

package org.panda_lang.panda.framework.language.runtime.expression;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeUtils;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionValueType;

import java.security.InvalidParameterException;

public class PandaExpression implements Expression {

    private final ExpressionValueType type;
    private final ClassPrototype returnType;
    private final DynamicExpression callback;
    private final Value value;

    public PandaExpression(Value value) {
        this(ExpressionValueType.KNOWN, value.getType(), null, value);
    }

    public PandaExpression(DynamicExpression callback) {
        this(ExpressionValueType.UNKNOWN, callback.getReturnType(), callback, null);
    }

    protected PandaExpression(ExpressionValueType type, ClassPrototype returnType, DynamicExpression callback, Value value) {
        if (type == null) {
            throw new InvalidParameterException("ExpressionType cannot be null");
        }

        if (callback == null && value == null) {
            throw new InvalidParameterException("Callback and Value cannot be null at the same time");
        }

        if (value != null && !ClassPrototypeUtils.isAssignableFrom(returnType, value.getType())) {
            throw new InvalidParameterException("Incompatible expression types " + returnType + " != " + value.getType());
        }

        this.type = type;
        this.returnType = returnType;
        this.callback = callback;
        this.value = value;
    }

    @Override
    public Value evaluate(Flow flow) {
        if (type == ExpressionValueType.UNKNOWN) {
            return callback.call(this, flow);
        }

        return value;
    }

    @Override
    public ClassPrototype getReturnType() {
        return returnType;
    }

    @Override
    public ExpressionValueType getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = type.name() + ":" + (returnType != null ? returnType.getName() : "any");
        return ExpressionValueType.KNOWN == type ? s + ":" + value.getValue() : s;
    }

}
