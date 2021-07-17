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

package panda.interpreter.runtime;

import org.jetbrains.annotations.Nullable;

/**
 * Result of invocation with status code
 *
 * @param <T> generic type of result
 */
public final class Result<T> {

    private final byte status;
    private final T result;

    public Result(byte status, T result) {
        this.status = status;
        this.result = result;
    }

    /**
     * Get result value
     *
     * @return the value
     */
    public @Nullable T getResult() {
        return result;
    }

    /**
     * Get status code
     *
     * @return the status code
     * @see panda.interpreter.runtime.Status
     */
    public byte getStatus() {
        return status;
    }

}
