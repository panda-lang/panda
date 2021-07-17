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

import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.runtime.ProcessStack;
import panda.std.Lazy;

import java.security.InvalidParameterException;

public class PandaExpression implements Expression {

    private final ExpressionValueType type;
    private final Lazy<Signature> returnType;
    private final ExpressionEvaluator evaluator;
    private final Object value;

    public PandaExpression(Signature returnType, Object value) {
        this(ExpressionValueType.CONST, new Lazy<>(returnType), null, value);
    }

    public PandaExpression(DynamicExpression expression) {
        this(ExpressionValueType.DYNAMIC, new Lazy<>(expression::getReturnType), expression, null);
    }

    protected PandaExpression(ExpressionValueType type, Lazy<Signature> returnType, ExpressionEvaluator evaluator, Object value) {
        if (type == null) {
            throw new InvalidParameterException("ExpressionType cannot be null");
        }

        this.type = type;
        this.returnType = returnType;
        this.evaluator = evaluator;
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        return type == ExpressionValueType.CONST ? value : evaluator.evaluate(stack, instance);
    }

    @Override
    public Signature getSignature() {
        return returnType.get();
    }

    @Override
    public ExpressionValueType getExpressionType() {
        return type;
    }

    @Override
    public String toString() {
        String s = type.name() + ":" + (returnType != null ? returnType : "any");
        return ExpressionValueType.CONST == type ? s + ":" + value : s;
    }

}
