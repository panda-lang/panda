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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;

import java.util.function.Supplier;

public class ExpressionResult<T> {

    private static final ExpressionResult EMPTY = of(null);

    private final @Nullable T value;

    private final @Nullable String errorMessage;
    private final @Nullable TokenRepresentation source;

    ExpressionResult(@Nullable T value, TokenRepresentation source, String errorMessage) {
        this.value = value;
        this.source = source;
        this.errorMessage = errorMessage;
    }

    ExpressionResult(@Nullable T value) {
        this(value, null, null);
    }

    ExpressionResult(String errorMessage, TokenRepresentation source) {
        this(null, source, errorMessage);
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean containsError() {
        return source != null && errorMessage != null;
    }

    public T orElse(T elseValue) {
        return isPresent() ? value : elseValue;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return isPresent() ? value : supplier.get();
    }

    public T get() {
        return value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TokenRepresentation getSource() {
        return source;
    }

    public static <T> ExpressionResult<T> error(String message, TokenRepresentation source) {
        return new ExpressionResult<>(message, source);
    }

    public static <T> ExpressionResult<T> of(T value) {
        return new ExpressionResult<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> ExpressionResult<T> empty() {
        return EMPTY;
    }

}
