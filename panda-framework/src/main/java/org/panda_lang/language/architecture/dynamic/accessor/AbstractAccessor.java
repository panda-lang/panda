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

package org.panda_lang.language.architecture.dynamic.accessor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.runtime.MemoryContainer;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.function.ThrowingBiFunction;

public abstract class AbstractAccessor<T extends Variable> implements Accessor<T> {

    private final ThrowingBiFunction<ProcessStack, Object, MemoryContainer, Exception> memory;
    private final T variable;
    private final int pointer;

    public AbstractAccessor(ThrowingBiFunction<ProcessStack, Object, MemoryContainer, Exception> memory, T variable, int internalPointer) {
        if (internalPointer == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        this.memory = memory;
        this.variable = variable;
        this.pointer = internalPointer;
    }

    @Override
    public MemoryContainer fetchMemoryContainer(ProcessStack stack, Object instance) throws Exception {
        return memory.apply(stack, instance);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object getValue(ProcessStack stack, Object instance) throws Exception {
        return fetchMemoryContainer(stack, instance).get(pointer);
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
