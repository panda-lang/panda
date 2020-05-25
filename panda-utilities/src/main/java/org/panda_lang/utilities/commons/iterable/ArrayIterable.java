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

package org.panda_lang.utilities.commons.iterable;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Iterator;

public final class ArrayIterable<T> implements Iterable<T> {

    private final Object array;
    private final int length;

    public ArrayIterable(Object array) {
        this.array = array;
        this.length = Array.getLength(array);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    final class ArrayIterator implements Iterator<T> {

        private int index;

        @Override
        public boolean hasNext() {
            return index < length;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            return (T) Array.get(array, index++);
        }

    }

}
