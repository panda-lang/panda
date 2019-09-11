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

package org.panda_lang.panda.language.architecture.dynamic.accessor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;

import java.util.function.Function;

abstract class DefaultAccessor<T extends Variable> implements Accessor<T> {

    private final Function<Flow, MemoryContainer> memory;
    private final T variable;
    private final int pointer;

    public DefaultAccessor(Function<Flow, MemoryContainer> memory, T variable, int internalPointer) {
        if (internalPointer == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        this.memory = memory;
        this.variable = variable;
        this.pointer = internalPointer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object perform(Flow flow, AccessorVisitor callback) {
        MemoryContainer memory = fetchMemoryContainer(flow);
        return memory.set(pointer, callback.visit(this, flow, memory.get(pointer)));
    }

    @Override
    public MemoryContainer fetchMemoryContainer(Flow flow) {
        return memory.apply(flow);
    }

    protected Function<Flow, MemoryContainer> getMemoryFunction() {
        return memory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object getValue(Flow flow) {
        return fetchMemoryContainer(flow).get(pointer);
    }

    @Override
    public int getMemoryPointer() {
        return pointer;
    }

    @Override
    public T getVariable() {
        return variable;
    }

}
