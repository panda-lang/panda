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

package org.panda_lang.utilities.commons.text;

import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.UnsafeUtils;
import panda.std.Option;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Formatter {

    private final Map<String, Supplier<?>> placeholders;
    private final Function<String, Option<Exception>> verifier;

    public Formatter(Map<String, Supplier<?>> placeholders, Function<String, Option<Exception>> verifier) {
        this.placeholders = placeholders;
        this.verifier = verifier;
    }

    public Formatter(Function<String, Option<Exception>> verifier) {
        this(new LinkedHashMap<>(), verifier);
    }

    public Formatter() {
        this(new LinkedHashMap<>(), message -> Option.none());
    }

    public String format(String message) {
        for (Map.Entry<String, Supplier<?>> placeholderEntry : placeholders.entrySet()) {
            String key = placeholderEntry.getKey();

            if (!message.contains(key)) {
                continue;
            }

            Object value = placeholderEntry.getValue().get();

            if (value == null) {
                throw new NullPointerException("Placeholder " + key + " returns null value");
            }

            message = StringUtils.replace(message, key, Objects.toString(value));
        }

        Option<Exception> verificationResult = verifier.apply(message);

        if (verificationResult.isPresent()) {
            return UnsafeUtils.throwException(verificationResult.get());
        }

        return message;
    }

    public Formatter register(String placeholder, Object value) {
        return register(placeholder, value::toString);
    }

    public Formatter register(String placeholder, Supplier<?> value) {
        this.placeholders.put(placeholder, value);
        return this;
    }

    public Formatter fork() {
        return new Formatter(new LinkedHashMap<>(this.placeholders), verifier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String placeholder) {
        Supplier<?> supplier = placeholders.get(placeholder);
        return (T) supplier.get();
    }

}
