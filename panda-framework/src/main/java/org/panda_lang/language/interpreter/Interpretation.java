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

package org.panda_lang.language.interpreter;

import org.panda_lang.language.interpreter.messenger.LoggerHolder;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;
import org.panda_lang.utilities.commons.function.ThrowingSupplier;

import java.util.Collection;

/**
 * Represents process of interpretation
 */
public interface Interpretation extends LoggerHolder {

    /**
     * Execute the given task
     *
     * @param task the task to execute
     * @param <E> exception type
     * @return the interpretation instance
     */
    <E extends Exception> Interpretation execute(ThrowingRunnable<E> task);

    /**
     * Execute the given task and get result
     *
     * @param task the task to execute
     * @param <T> type of result
     * @param <E> exception type
     * @return the interpretation instance
     */
    <T, E extends Exception> T execute(ThrowingSupplier<T, E> task);

    /**
     * Check if process of interpretation is still healthy (executed tasks didn't throw any exceptions)
     *
     * @return true if healthy, otherwise false
     */
    boolean isHealthy();

    /**
     * Get all collected failures
     *
     * @return collection of failures
     */
    Collection<? extends Exception> getFailures();

    /**
     * Get associated interpreter
     *
     * @return the interpreter
     */
    Interpreter getInterpreter();

}
