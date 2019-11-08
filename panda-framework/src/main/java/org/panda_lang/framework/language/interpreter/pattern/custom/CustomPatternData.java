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

package org.panda_lang.framework.language.interpreter.pattern.custom;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.language.interpreter.pattern.PatternMapping;

import java.util.HashMap;
import java.util.Map;

public final class CustomPatternData implements PatternMapping {

    private final Map<String, Object> data = new HashMap<>();

    public CustomPatternData with(String id, Object value) {
        this.data.put(id, value);
        return this;
    }

    public boolean has(String id) {
        return data.containsKey(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String id) {
        T value = (T) data.get(id);

        if (value == null) {
            throw new PandaFrameworkException("Cannot find custom pattern data with id: " + id);
        }

        return value;
    }

}
