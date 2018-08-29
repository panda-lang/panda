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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MessageFormatter {

    private final Map<String, Supplier<?>> placeholders;

    public MessageFormatter(Map<String, Supplier<?>> placeholders) {
        this.placeholders = placeholders;
    }

    public MessageFormatter() {
        this(new HashMap<>());
    }

    public String format(String message) {
        for (Map.Entry<String, Supplier<?>> placeholderEntry : placeholders.entrySet()) {
            String key = placeholderEntry.getKey();
            Object value = placeholderEntry.getValue().get();

            if (!message.contains(key)) {
                continue;
            }

            message = StringUtils.replace(message, key, value != null ? value.toString() : "<value not specified>");
        }

        return message;
    }

    public MessageFormatter register(String placeholder, Object value) {
        return register(placeholder, value::toString);
    }

    public MessageFormatter register(String placeholder, Supplier<?> value) {
        this.placeholders.put(placeholder, value);
        return this;
    }

    public MessageFormatter fork() {
        return new MessageFormatter(new HashMap<>(this.placeholders));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String placeholder) {
        Supplier<?> supplier = placeholders.get(placeholder);
        return (T) supplier.get();
    }

}
