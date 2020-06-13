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

import java.util.Iterator;

public class SingletonIterator<T> implements Iterator<T> {

    private final T element;
    private int count;

    public SingletonIterator(T element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return count == 0;
    }

    @Override
    public T next() {
        count++;
        return element;
    }

}
