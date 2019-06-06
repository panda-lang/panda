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

package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.messenger.FormatterFunction;
import org.panda_lang.panda.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.StreamUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

final class PandaMessengerFormatter implements MessengerFormatter {

    private final Messenger messenger;
    private final Map<Class<?>, Map<String, FormatterFunction>> placeholders;

    public PandaMessengerFormatter(Messenger messenger) {
        this(messenger, new HashMap<>());
    }

    private PandaMessengerFormatter(Messenger messenger, Map<Class<?>, Map<String, FormatterFunction>> placeholders) {
        this.messenger = messenger;
        this.placeholders = placeholders;
    }

    @Override
    public String format(String message, Object... values) {
        return placeholders.entrySet().stream()
                .map(entry -> toEntry(entry, values))
                .filter(Objects::nonNull)
                .flatMap(data -> Stream.concat(createFunction(data), createFunction(Maps.immutableEntryOf(placeholders.get(null), null))))
                .reduce(message, (content, function) -> function.apply(content), StreamUtils.emptyBinaryOperator());
    }

    private @Nullable Map.Entry<Map<String, FormatterFunction>, Object> toEntry(Map.Entry<Class<?>, Map<String, FormatterFunction>> entry, Object[] values) {
        Object data = ArrayUtils.findIn(values, value -> ClassUtils.isAssignableFrom(entry.getKey(), value));
        return data != null ? Maps.immutableEntryOf(entry.getValue(), data) : null;
    }

    private Stream<Function<String, String>> createFunction(Map.Entry<Map<String, FormatterFunction>, Object> data) {
        return data.getKey().entrySet().stream().map(entry -> createFormatterFunction(entry, data.getValue()));
    }

    @SuppressWarnings("unchecked")
    private Function<String, String> createFormatterFunction(Map.Entry<String, FormatterFunction> entry, Object data) {
        return message -> {
            Object value = entry.getValue().apply(this, data);

            if (value == null) {
                value = "<placeholder error: " + entry.getKey() + " == null>";
            }

            return StringUtils.replace(message, entry.getKey(), value.toString());
        };
    }

    @Override
    public <T> MessengerFormatter register(String placeholder, @Nullable Class<T> requiredData, FormatterFunction<?> replacementFunction) {
        placeholders.computeIfAbsent(requiredData, key -> new HashMap<>()).put(placeholder, replacementFunction);
        return this;
    }

    @Override
    public MessengerFormatter fork() {
        return new PandaMessengerFormatter(messenger, new HashMap<>(placeholders));
    }

    @Override
    public <T> MessengerTypeFormatter<T> getTypeFormatter(Class<T> type) {
        return new PandaMessengerTypeFormatter<>(this, type);
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

}
