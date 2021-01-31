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

package org.panda_lang.framework.interpreter.parser.expression;

/**
 * Type of subparser
 */
public enum ExpressionSubparserType {

    /**
     * Requires only source
     */
    INDIVIDUAL(0),

    /**
     * Requires source and may require previously parsed expression on stack
     */
    MODERATE(1),

    /**
     * Requires expression on stack
     */
    MUTUAL(2);

    private final int priority;

    ExpressionSubparserType(int priority) {
        this.priority = priority;
    }

    /**
     * Get priority of type
     *
     * @return the priority represented by int, the lowest is executed as first
     */
    protected int getPriority() {
        return priority;
    }

}
