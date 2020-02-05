/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.array;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.architecture.type.array.ArrayType;

import java.lang.reflect.Array;

public final class ArrayAccessor implements DynamicExpression {

    private final Type type;
    private final Expression instanceExpression;
    private final Expression indexExpression;

    public ArrayAccessor(Expression instanceExpression, Expression indexExpression) {
        if (!(instanceExpression.getType() instanceof ArrayType)) {
            throw new PandaFrameworkException("Array required");
        }

        this.type = ((ArrayType) instanceExpression.getType()).getArrayType();
        this.instanceExpression = instanceExpression;
        this.indexExpression = indexExpression;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        return Array.get(getArrayInstance(stack, instance), getIndex(stack, instance));
    }

    public ArrayAssigner toAssignerExpression(Expression value) {
        return new ArrayAssigner(this, value);
    }

    public Integer getIndex(ProcessStack stack, Object instance) throws Exception {
        return indexExpression.evaluate(stack, instance);
    }

    public Object getArrayInstance(ProcessStack stack, Object instance) throws Exception {
        return instanceExpression.evaluate(stack, instance);
    }

    @Override
    public Type getReturnType() {
        return type;
    }

}
