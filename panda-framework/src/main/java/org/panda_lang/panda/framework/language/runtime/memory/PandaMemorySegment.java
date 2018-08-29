/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.runtime.memory;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.runtime.memory.MemorySegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaMemorySegment implements MemorySegment {

    private final int typeID;
    private final String type;
    private final AtomicInteger instanceIDAssigner;
    private final Stack<Integer> free;
    private final List<Object> instances;

    public PandaMemorySegment(int typeID, String type) {
        this.typeID = typeID;
        this.type = type;
        this.instanceIDAssigner = new AtomicInteger();
        this.free = new Stack<>();
        this.instances = new ArrayList<>();
    }

    @Override
    public int put(Object value) {
        if (free.size() > 0) {
            int id = free.pop();
            instances.set(id, value);
            return id;
        }
        else {
            int id = instanceIDAssigner.getAndIncrement();
            fillAndSet(id, value);
            return id;
        }
    }

    protected void fillAndSet(int index, Object value) {
        if (index > (instances.size() - 1)) {
            for (int i = instances.size(); i < index; i++) {
                instances.add(null);
            }
            instances.add(value);
        }
        else {
            instances.set(index, value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T destroy(int pointer) {
        if (instances.size() > pointer) {
            Object element = instances.remove(pointer);
            free.push(pointer);
            return (T) element;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T get(int pointer) {
        return instances.size() > pointer ? (T) instances.get(pointer) : null;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getTypeID() {
        return typeID;
    }

}
