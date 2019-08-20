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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

final class ArrayAccessorMemoryContainer implements MemoryContainer {

    private final ArrayAccessor accessor;
    private final Flow flow;

    public ArrayAccessorMemoryContainer(ArrayAccessor accessor, Flow flow) {
        this.accessor = accessor;
        this.flow = flow;
    }

    @Override
    public @Nullable Value set(int pointer, @Nullable Value value) {
        getArray()[pointer] = value != null ? value.getObject() : null;
        return value;
    }

    @NotNull
    @Override
    public Value get(int pointer) {
        Object[] array = getArray();
        Integer i = accessor.index.evaluate(flow).getValue();

        Object value = array[i];
        return new PandaStaticValue(accessor.type, value);
    }

    private Object[] getArray() {
        return accessor.instance.evaluate(flow).getValue();
    }

    @Override
    public int getAmountOfVariables() {
        return 0;
    }
    
}
