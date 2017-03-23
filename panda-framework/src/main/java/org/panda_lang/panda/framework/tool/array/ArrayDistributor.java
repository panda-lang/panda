/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.framework.tool.array;

import java.util.Iterator;

public class ArrayDistributor<T> implements Iterator<T>, Iterable<T> {

    private final T[] array;
    private int index;

    public ArrayDistributor(T[] array) {
        this.array = array;
        this.index = -1;
    }

    public void reset() {
        this.index = -1;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    public T previous() {
        if (index - 1 < array.length) {
            --index;

            if (index < 0) {
                index = 0;
            }

            return array[index];
        }

        return null;
    }

    public T current() {
        return index < array.length ? array[index] : null;
    }

    @Override
    public boolean hasNext() {
        return index < array.length - 1;
    }

    @Override
    public T next() {
        if (index + 1 < array.length) {
            ++index;
            return array[index];
        }

        return null;
    }

    public T further() {
        if (index + 1 < array.length) {
            return array[index + 1];
        }

        return null;
    }

    public T future() {
        if (index + 2 < array.length) {
            return array[index + 2];
        }

        return null;
    }

    public T get(int index) {
        return index > -1 && index < array.length ? array[index] : null;
    }

    public T getPrevious(int t) {
        return index - t > 0 ? array[index - t] : null;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getPrevious() {
        return index - 1 > 0 ? array[index - 1] : null;
    }

    public T getLast() {
        return array[array.length - 1];
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return array.length;
    }

}
