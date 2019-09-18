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

package org.panda_lang.utilities.commons.function;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Produce<T, E> {

    private final T result;
    private final Supplier<E> error;

    private Produce(T result, Supplier<E> error) {
        this.result = result;
        this.error = error;
    }

    public Produce(T result) {
        this(result, null);
    }

    public Produce(Supplier<E> error) {
        this(null, error);
    }

    public void ifPresent(Consumer<T> consumer) {
        consumer.accept(result);
    }

    public boolean hasResult() {
        return result != null;
    }

    public boolean hasError() {
        return !hasResult();
    }

    public @Nullable E getError() {
        return error != null ? error.get() : null;
    }

    public T getResult() {
        return result;
    }

}
