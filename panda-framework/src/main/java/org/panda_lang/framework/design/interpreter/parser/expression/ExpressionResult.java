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

package org.panda_lang.framework.design.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.architecture.expression.Expression;

import java.util.function.Supplier;

public class ExpressionResult {

    private static final ExpressionResult EMPTY = of(null);

    private final @Nullable Expression value;
    private final @Nullable String errorMessage;
    private final @Nullable TokenRepresentation source;

    ExpressionResult(@Nullable Expression value, TokenRepresentation source, String errorMessage) {
        this.value = value;
        this.source = source;
        this.errorMessage = errorMessage;
    }

    ExpressionResult(@Nullable Expression value) {
        this(value, null, null);
    }

    ExpressionResult(String errorMessage, TokenRepresentation source) {
        this(null, source, errorMessage);
    }

    public boolean isEmpty() {
        return !isPresent();
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean containsError() {
        return source != null && errorMessage != null;
    }

    public Expression orElse(Expression elseValue) {
        return isPresent() ? value : elseValue;
    }

    public Expression orElseGet(Supplier<? extends Expression> supplier) {
        return isPresent() ? value : supplier.get();
    }

    public Expression get() {
        return value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TokenRepresentation getSource() {
        return source;
    }

    public static ExpressionResult error(String message, ExpressionContext context) {
        context.getSynchronizedSource().next(-1);
        return error(message, context.getSynchronizedSource().getAvailableSource());
    }

    public static ExpressionResult error(String message, Snippet source) {
        if (source.isEmpty()) {
            throw new IllegalArgumentException("Source cannot be empty");
        }

        return new ExpressionResult(message, source.getFirst());
    }

    public static ExpressionResult error(String message, TokenRepresentation source) {
        return new ExpressionResult(message, source);
    }

    public static ExpressionResult of(Expression value) {
        return new ExpressionResult(value);
    }

    public static ExpressionResult empty() {
        return EMPTY;
    }

}
