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
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionType;

import java.security.InvalidParameterException;

public class PandaExpression implements Expression {

    private final ExpressionType type;
    private final ClassPrototype returnType;
    private final ExpressionCallback callback;
    private final Value value;

    public PandaExpression(Value value) {
        this(ExpressionType.KNOWN, value.getType(), null, value);
    }

    public PandaExpression(ExpressionCallback callback) {
        this(ExpressionType.UNKNOWN, callback.getReturnType(), callback, null);
    }

    protected PandaExpression(ExpressionType type, ClassPrototype returnType, ExpressionCallback callback, Value value) {
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
    public Value evaluate(Frame frame) {
        if (type == ExpressionType.UNKNOWN || type == ExpressionType.BOTH) {
            return callback.call(this, frame);
        }

        return value;
    }

    @Override
    public ClassPrototype getReturnType() {
        return returnType;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }

    @Override
    public String toString() {
        String s = type.name() + ":" + (returnType != null ? returnType.getName() : "any");
        return ExpressionType.KNOWN == type ? s + ":" + value.getValue() : s;
    }

}
