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

package org.panda_lang.language.interpreter.pattern.functional;

import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PatternData {

    private static final PatternData EMPTY = new PatternData(0);

    private final Map<String, Object> data;

    PatternData(int size) {
        this.data = size == 0 ? Collections.emptyMap() : new HashMap<>(size);
    }

    public PatternData with(String id, Object value) {
        this.data.put(id, value);
        return this;
    }

    public <T> Option<T> get(String id, Class<T> type) {
        return get(id);
    }

    @SuppressWarnings("unchecked")
    public <T> Option<T> get(String id) {
        T value = (T) data.get(id);

        if (value == null) {
            throw new PandaFrameworkException("Cannot find custom pattern data with id: " + id);
        }

        return Option.of(value);
    }

    public static PatternData capacity(int size) {
        return new PatternData(size);
    }

    public static PatternData empty() {
        return EMPTY;
    }

}
