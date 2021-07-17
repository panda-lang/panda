/*
 * Copyright (c) 2021 dzikoysk
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

package panda.utilities.text;

import panda.utilities.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Joiner {

    private final String separator;
    private final StringBuilder builder;

    private Joiner(String separator) {
        this.separator = separator;
        this.builder = new StringBuilder();
    }

    public <T> Joiner join(T[] elements, Function<T, ?> mapper) {
        return join(Arrays.asList(elements), mapper);
    }

    public <T> Joiner join(T[] elements, BiFunction<Integer, T, ?> mapper) {
        return join(Arrays.asList(elements), mapper);
    }

    public <T> Joiner join(Iterable<T> elements, Function<T, ?> mapper) {
        for (T element : elements) {
            append(mapper.apply(element));
        }

        return this;
    }

    public <T> Joiner join(Iterable<T> elements, BiFunction<Integer, T, ?> mapper) {
        int index = 0;

        for (T element : elements) {
            append(mapper.apply(index++, element));
        }

        return this;
    }

    @SafeVarargs
    public final <T> Joiner join(T... elements) {
        return join(Arrays.asList(elements));
    }

    public Joiner join(Collection<?> elements) {
        for (Object element : elements) {
            append(element);
        }

        return this;
    }

    public Joiner append(Object element) {
        builder.append(element).append(separator);
        return this;
    }

    @Override
    public String toString() {
        return builder.length() == 0
                ? StringUtils.EMPTY
                : builder.substring(0, builder.length() - separator.length());
    }

    public static Joiner on(String separator) {
        return new Joiner(separator);
    }

}
