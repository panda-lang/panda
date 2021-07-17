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

package org.panda_lang.panda.shell.repl;

import panda.utilities.ArrayUtils;

/**
 * Result of evaluation
 */
public final class ReplResult {

    static final ReplResult NONE = new ReplResult(null, null);

    /**
     * Type of result
     */
    public enum Type {
        /**
         * Represents value returned by REPL
         */
        SHELL,
        /**
         * Represents value returned by evaluation of panda expression
         */
        PANDA
    }

    private final Type type;
    private final Object result;

    ReplResult(Type type, Object result) {
        this.type = type;
        this.result = result;
    }

    /**
     * Check if result is array
     *
     * @return true if object is not null and its class represents array type
     */
    public boolean isArray() {
        return ArrayUtils.isArray(result);
    }

    /**
     * Get result of evaluation
     *
     * @return the result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Get type of result
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return (getType() == Type.PANDA ? "> " : "") + result;
    }

}
