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

package org.panda_lang.utilities.commons.collection;

import java.util.Arrays;

public final class FixedStack<T> implements IStack<T> {

    private final T[] stack;
    private int index;

    @SuppressWarnings("unchecked")
    public FixedStack(int size) {
        this.stack = (T[]) new Object[size];
    }

    @Override
    public void push(T element) {
        stack[index] = element;
        index++;
    }

    @Override
    public T peek() {
        return stack[index - 1];
    }

    @Override
    public T pop() {
        T object = stack[index - 1];
        index--;
        return object;
    }

    @Override
    public int size() {
        return index;
    }

    @Override
    public boolean isEmpty() {
        return index == 0;
    }

    @Override
    public void clear() {
        this.index = 0;
    }

    @Override
    public final T[] toArray(Class<T[]> arrayType) {
        Object[] array = new Object[size()];

        for (int stackIndex = size() - 1, arrayIndex = 0; stackIndex > -1; stackIndex--, arrayIndex++) {
            array[arrayIndex] = stack[stackIndex];
        }

        return Arrays.copyOf(array, array.length, arrayType);
    }

}
