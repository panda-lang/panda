/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.extractor.processed;

import org.jetbrains.annotations.Nullable;

public class ProcessedValue<T> {

    private final T value;
    private final String identifier;

    public ProcessedValue(T value, @Nullable String identifier) {
        this.value = value;
        this.identifier = identifier;
    }

    public @Nullable String getIdentifier() {
        return identifier;
    }

    public T getValue() {
        return value;
    }

}
