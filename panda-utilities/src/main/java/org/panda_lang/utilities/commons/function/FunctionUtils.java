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

package org.panda_lang.utilities.commons.function;

import java.util.function.Function;

public final class FunctionUtils {

    private FunctionUtils() { }

    /**
     * Map throwing function in default stream api with exception consume support
     *
     * @param function the function to apply
     * @param exceptionConsumer the exception consumer
     * @param <T> type of function parameter
     * @param <R> type of function result
     * @param <E> type of exception
     * @return a new function
     */
    @SuppressWarnings("unchecked")
    public static <T, R, E extends Exception> Function<T, R> map(ThrowingFunction<T, R, E> function, Function<E, R> exceptionConsumer) {
        return value -> {
            try {
                return function.apply(value);
            } catch (Exception exception) {
                return exceptionConsumer.apply((E) exception);
            }
        };
    }

}
