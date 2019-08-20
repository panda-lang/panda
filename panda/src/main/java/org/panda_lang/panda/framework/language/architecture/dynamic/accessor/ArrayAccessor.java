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

package org.panda_lang.panda.framework.language.architecture.dynamic.accessor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.AccessorVisitor;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.ArrayAssigner;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;

public final class ArrayAccessor implements Accessor {

    protected final ClassPrototype type;
    protected final Expression instance;
    protected final Expression index;

    public ArrayAccessor(Expression instance, Expression index) {
        if (!(instance.getReturnType() instanceof ArrayClassPrototype)) {
            throw new RuntimeException("Array required");
        }

        this.type = ((ArrayClassPrototype) instance.getReturnType()).getType().fetch();
        this.instance = instance;
        this.index = index;
    }

    @Override
    public @Nullable Value perform(Flow flow, AccessorVisitor callback) {
        Value wrappedValue = new ArrayAccessorMemoryContainer(this, flow).get(index.evaluate(flow).getValue());

        callback.visit(this, flow, wrappedValue);
        return wrappedValue;
    }

    @Override
    public MemoryContainer fetchMemoryContainer(Flow flow) {
        return new ArrayAccessorMemoryContainer(this, flow);
    }

    @Override
    public @Nullable Value getValue(Flow flow) {
        return new ArrayAccessorMemoryContainer(this, flow).get(index.evaluate(flow).getValue());
    }

    @Override
    public int getMemoryPointer() {
        return -1;
    }

    @Override
    public @Nullable Variable getVariable() {
        return null;
    }

    @Override
    public ClassPrototypeReference getTypeReference() {
        return type.getReference();
    }

    @Override
    public ArrayAssigner toAssigner(Expression value) {
        return new ArrayAssigner(this, index, value);
    }

}
