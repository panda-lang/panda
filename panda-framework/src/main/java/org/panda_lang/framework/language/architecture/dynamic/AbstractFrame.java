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

package org.panda_lang.framework.language.architecture.dynamic;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.statement.FramedScope;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

public abstract class AbstractFrame<T extends FramedScope> implements Frame {

    protected final T frame;
    protected final Object[] localMemory;

    protected AbstractFrame(T frame, int memorySize) {
        this.frame = frame;
        this.localMemory = new Object[memorySize];
    }

    protected AbstractFrame(T frame) {
        this(frame, frame.getRequiredMemorySize());
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= localMemory.length) {
            throw new PandaRuntimeException("Invalid variable index: " + index + "; Amount of localMemory: " + getMemorySize());
        }
    }

    @Override
    public synchronized <R> R set(int pointer, @Nullable R value) {
        checkIndex(pointer);
        localMemory[pointer] = value;
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized @Nullable <R> R get(int pointer) {
        checkIndex(pointer);
        return (R) localMemory[pointer];
    }

    @Override
    public int getMemorySize() {
        return localMemory.length;
    }

    @Override
    public T getFramedScope() {
        return frame;
    }

}
