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

package org.panda_lang.panda.utilities.commons.redact;

import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class ContentJoiner {

    private final String separator;
    private final StringBuilder builder;

    private ContentJoiner(String separator) {
        this.separator = separator;
        this.builder = new StringBuilder();
    }

    public <T> ContentJoiner join(T[] elements, Function<T, ?> mapper) {
        return join(Arrays.asList(elements), mapper);
    }

    public <T> ContentJoiner join(Collection<T> elements, Function<T, ?> mapper) {
        for (T element : elements) {
            append(mapper.apply(element));
        }

        return this;
    }

    public ContentJoiner join(Object... elements) {
        return join(Arrays.asList(elements));
    }

    public ContentJoiner join(Collection<?> elements) {
        for (Object element : elements) {
            append(element);
        }

        return this;
    }

    public ContentJoiner append(Object element) {
        builder.append(element).append(separator);
        return this;
    }

    @Override
    public String toString() {
        return builder.length() == 0 ? StringUtils.EMPTY : builder.substring(0, builder.length() - separator.length());
    }

    public static ContentJoiner on(String separator) {
        return new ContentJoiner(separator);
    }

}
