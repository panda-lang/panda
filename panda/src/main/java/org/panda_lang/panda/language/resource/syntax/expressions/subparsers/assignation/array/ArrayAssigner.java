/*
 * Copyright (c) 2020 Dzikoysk
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

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.architecture.expression.DynamicExpression;
import org.panda_lang.language.runtime.PandaRuntimeException;

import java.lang.reflect.Array;

public final class ArrayAssigner implements DynamicExpression {

    private final ArrayAccessor accessor;
    private final Expression value;

    public ArrayAssigner(ArrayAccessor accessor, Expression value) {
        this.accessor = accessor;
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        Integer index = accessor.getIndex(stack, instance);

        if (index == null) {
            throw new PandaRuntimeException("Index cannot be null");
        }

        Object value = this.value.evaluate(stack, instance);
        Array.set(accessor.getArrayInstance(stack, instance), index, value);

        return value;
    }

    @Override
    public Type getReturnType() {
        return accessor.getReturnType();
    }

}
