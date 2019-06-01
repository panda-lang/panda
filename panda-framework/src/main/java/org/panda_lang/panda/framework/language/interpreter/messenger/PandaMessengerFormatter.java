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
import org.panda_lang.panda.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ClassUtils;
import org.panda_lang.panda.utilities.commons.StreamUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

final class PandaMessengerFormatter implements MessengerFormatter {

    private final Messenger messenger;
    private final TreeMap<Class<?>, Map<String, BiFunction<MessengerFormatter, Object, String>>> placeholders;

    public PandaMessengerFormatter(Messenger messenger) {
        this(messenger, new TreeMap<>(ClassUtils.CLASS_ASSIGNATION_COMPARATOR));
    }

    private PandaMessengerFormatter(Messenger messenger, TreeMap<Class<?>, Map<String, BiFunction<MessengerFormatter, Object, String>>> placeholders) {
        this.messenger = messenger;
        this.placeholders = placeholders;
    }

    @Override
    public String format(String message, Object... values) {
        return placeholders.entrySet().stream()
                .map(entry -> Maps.immutableEntryOf(entry.getValue(), ArrayUtils.findIn(values, value -> ClassUtils.isAssignableFrom(entry.getKey(), value))))
                .flatMap(this::createFormatterFunctions)
                .reduce(message, (content, function) -> function.apply(content), StreamUtils.emptyBinaryOperator());
    }

    private Stream<Function<String, String>> createFormatterFunctions(Map.Entry<Map<String, BiFunction<MessengerFormatter, Object, String>>, Object> data) {
        return data.getKey().entrySet().stream().map(entry -> createFormatterFunction(entry, data.getValue()));
    }

    private Function<String, String> createFormatterFunction(Map.Entry<String, BiFunction<MessengerFormatter, Object, String>> entry, Object data) {
        return message -> StringUtils.replace(message, entry.getKey(), entry.getValue().apply(this, data));
    }

    @Override
    public <T> MessengerFormatter register(String placeholder, @Nullable Class<T> requiredData, BiFunction<MessengerFormatter, Object, String> replacementFunction) {
        placeholders.computeIfAbsent(requiredData, key -> new HashMap<>()).put(placeholder, replacementFunction);
        return this;
    }

    @Override
    public MessengerFormatter fork() {
        return new PandaMessengerFormatter(messenger, new TreeMap<>(placeholders));
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

}
