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

package org.panda_lang.framework.language.interpreter.parser.pipeline;

import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public final class PandaLocalChannel implements LocalChannel {

    private static final int DEFAULT_SIZE = 8;

    private final Map<String, Object> data;

    public PandaLocalChannel() {
        this.data = new HashMap<>(DEFAULT_SIZE);
    }

    public PandaLocalChannel(LocalChannel channel) {
        this.data = new HashMap<>(channel.getData().size() + DEFAULT_SIZE);
        this.data.putAll(channel.getData());
    }

    @Override
    public <T> T allocated(String identifier, T value) {
        Object previous = data.put(identifier, value);

        if (previous != null) {
            throw new IllegalStateException("Duplicated identifier '" + identifier + "'");
        }

        return value;
    }

    @Override
    public <T> T override(String identifier, T value) {
        data.put(identifier, value);
        return value;
    }

    @Override
    public boolean contains(Class<?> type) {
        for (Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            if (value != null && type.isAssignableFrom(value.getClass())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(String identifier) {
        return data.containsKey(identifier);
    }

    @Override
    public <T> T get(Class<T> type) {
        for (Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            if (value != null && type.isAssignableFrom(value.getClass())) {
                return ObjectUtils.cast(value);
            }
        }

        throw new NoSuchElementException("Cannot find data matching " + type.getSimpleName() + " type in current channel");
    }

    @Override
    public <T> T get(String identifier, Class<T> type) {
        return get(identifier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String identifier) {
        return (T) Option.of(data.get(identifier)).orThrow(() -> {
            throw new NoSuchElementException("'" + identifier + "' value not found in channel");
        });
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

}
