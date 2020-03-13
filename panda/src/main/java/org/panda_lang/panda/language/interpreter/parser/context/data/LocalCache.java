/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.context.data;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class LocalCache {

    private final Map<String, Object> cache;

    public LocalCache() {
        this.cache = new HashMap<>();
    }

    public boolean hasValue(String element) {
        return cache.containsKey(element);
    }

    public <T> T allocated(T element) {
        return allocated(element.getClass().getSimpleName(), element);
    }

    public <T> T allocated(Class<T> clazz, T element) {
        return allocated(clazz.getSimpleName(), element);
    }

    public <T> T allocated(String name, T element) {
        cache.put(name, element);
        return element;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(String name) {
        return (T) cache.get(name);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(Class<T> type) {
        for (Object datum : cache.values()) {
            if (datum == null) {
                continue;
            }

            if (type.isAssignableFrom(datum.getClass())) {
                return (T) datum;
            }
        }

        return null;
    }

}
