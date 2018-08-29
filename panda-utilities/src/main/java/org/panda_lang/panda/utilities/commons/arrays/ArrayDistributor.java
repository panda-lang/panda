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

package org.panda_lang.panda.utilities.commons.arrays;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDistributor<T> implements Iterator<T>, Iterable<T> {

    private final T[] array;
    private int index;

    public ArrayDistributor(T[] array) {
        this.array = array.clone();
        this.index = -1;
    }

    @SuppressWarnings("unchecked")
    public ArrayDistributor(Collection<T> collection, Class<T> type) {
        this(collection.toArray((T[]) Array.newInstance(type, collection.size())));
    }

    public void reset() {
        this.index = -1;
    }

    public void reverse() {
        T[] copy = Arrays.copyOf(array, array.length);

        for (int i = 0, j = array.length - 1; i < array.length; i++, j--) {
            array[i] = copy[j];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    public @Nullable T previous() {
        if (index - 1 < array.length) {
            --index;

            if (index < 0) {
                index = 0;
            }

            return array[index];
        }

        return null;
    }

    public @Nullable T current() {
        return index < array.length && index > -1 ? array[index] : null;
    }

    @Override
    public @Nullable T next() {
        if (index + 1 < array.length) {
            return array[++index];
        }

        throw new NoSuchElementException();
    }

    public @Nullable T further() {
        if (index + 1 < array.length) {
            return array[index + 1];
        }

        return null;
    }

    public @Nullable T future() {
        if (index + 2 < array.length) {
            return array[index + 2];
        }

        return null;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean hasNext() {
        return index + 1 < array.length;
    }

    public @Nullable T get(int index) {
        return index > -1 && index < array.length ? array[index] : null;
    }

    public @Nullable T getPrevious(int t) {
        int i = index - t;
        return i > -1 && i < array.length ? array[i] : null;
    }

    public @Nullable T getPrevious() {
        int i = index - 1;
        return i > -1 && i - 1 < array.length ? array[i] : null;
    }

    public @Nullable T getLast() {
        return array[array.length - 1];
    }

    public @Nullable T getNext() {
        return index + 1 < array.length ? array[index + 1] : this.getLast();
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return array.length;
    }

}
