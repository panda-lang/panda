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
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;

final class ArrayAccessorMemoryContainer implements MemoryContainer {

    private final Object[] array;
    private final int index;

    public ArrayAccessorMemoryContainer(Flow flow, ArrayAccessor accessor, int index) {
        this.array = accessor.instance.evaluate(flow);
        this.index = index;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object set(int pointer, @Nullable Object value) {
        array[index] = value;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object get(int pointer) {
        return array[index];
    }

    @Override
    public int getAmountOfVariables() {
        return -1;
    }
    
}
