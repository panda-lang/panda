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

package org.panda_lang.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expressible;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.TokenInfo;

import java.util.function.Supplier;

/**
 * Result wrapper that may contain the result expression, error or nothing
 */
public final class ExpressionResult {

    private static final ExpressionResult EMPTY = new ExpressionResult(null);

    private final @Nullable Expression expression;
    private final @Nullable String errorMessage;
    private final @Nullable Snippetable source;

    ExpressionResult(@Nullable Expressible expressible, @Nullable Snippetable source, @Nullable String errorMessage) {
        this.expression = (expressible != null ? expressible.toExpression() : null);
        this.source = source;
        this.errorMessage = errorMessage;
    }

    ExpressionResult(@Nullable Expressible expressible) {
        this(expressible, null, null);
    }

    ExpressionResult(String errorMessage, TokenInfo source) {
        this(null, source, errorMessage);
    }

    /**
     * Check if wrapper does not contain expression
     *
     * @return true if wrapper contains result
     */
    public boolean isEmpty() {
        return !isPresent();
    }

    /**
     * Check if result is present
     *
     * @return true if the result expression is present, otherwise false
     */
    public boolean isPresent() {
        return expression != null;
    }

    /**
     * Check if result contains error
     *
     * @return true if result contains error data, otherwise false
     */
    public boolean containsError() {
        return source != null && errorMessage != null;
    }

    public Expression orElseGet(Supplier<? extends Expression> supplier) {
        return isPresent() ? expression : supplier.get();
    }

    /**
     * Get result expression
     *
     * @return the result expression
     */
    public Expression get() {
        return expression;
    }

    /**
     * Get error message
     *
     * @return the error message
     */
    public @Nullable String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Get indicated error source
     *
     * @return the source
     */
    public Snippetable getErrorSource() {
        return source;
    }

    /**
     * Create result
     *
     * @param expressible the result expression
     * @return the result
     */
    public static ExpressionResult of(Expressible expressible) {
        return new ExpressionResult(expressible.toExpression());
    }

    /**
     * Create error result
     *
     * @param message the error message
     * @param context the expression context
     * @return the error result
     */
    public static ExpressionResult error(String message, ExpressionContext<?> context) {
        context.getSynchronizedSource().next(-1);
        return error(message, context.getSynchronizedSource().getAvailableSource());
    }

    /**
     * Create error result
     *
     * @param message the error message
     * @param source the indicated source
     * @return the error result
     */
    public static ExpressionResult error(String message, Snippetable source) {
        Snippet snippet = source.toSnippet();

        if (snippet.isEmpty()) {
            throw new IllegalArgumentException("Error source cannot be empty");
        }

        return new ExpressionResult(message, snippet.getFirst());
    }

    /**
     * @return get empty result
     */
    public static ExpressionResult empty() {
        return EMPTY;
    }

}
