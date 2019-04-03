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
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

import java.util.function.Function;

abstract class DefaultAccessor<T extends Variable> implements Accessor<T> {

    private final Function<ExecutableBranch, MemoryContainer> memory;
    private final T variable;
    private final int pointer;

    public DefaultAccessor(Function<ExecutableBranch, MemoryContainer> memory, T variable, int internalPointer) {
        if (internalPointer == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        this.memory = memory;
        this.variable = variable;
        this.pointer = internalPointer;
    }

    @Override
    public Value perform(ExecutableBranch branch, AccessorCallback callback) {
        MemoryContainer memory = fetchMemoryContainer(branch);
        return memory.set(pointer, callback.visit(this, branch, memory.get(pointer)));
    }

    @Override
    public MemoryContainer fetchMemoryContainer(ExecutableBranch branch) {
        return memory.apply(branch);
    }

    protected Function<ExecutableBranch, MemoryContainer> getMemoryFunction() {
        return memory;
    }

    @Override
    public @Nullable Value getValue(ExecutableBranch branch) {
        return fetchMemoryContainer(branch).get(pointer);
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
