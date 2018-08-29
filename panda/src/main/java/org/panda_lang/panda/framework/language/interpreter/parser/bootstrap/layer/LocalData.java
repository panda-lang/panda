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

package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LocalData {

    private final Map<String, Object> data;

    public LocalData() {
        this.data = new HashMap<>();
    }

    public <T> T allocateInstance(T element) {
        return allocateInstance(element.getClass().getSimpleName(), element);
    }

    public <T> T allocateInstance(Class<T> clazz, T element) {
        return allocateInstance(clazz.getSimpleName(), element);
    }

    public <T> T allocateInstance(String name, T element) {
        data.put(name, element);
        return element;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(String name) {
        return (T) data.get(name);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T getValue(Class<T> type) {
        for (Object datum : data.values()) {
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
