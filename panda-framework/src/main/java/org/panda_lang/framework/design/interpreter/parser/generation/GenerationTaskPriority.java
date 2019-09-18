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

package org.panda_lang.framework.design.interpreter.parser.generation;

import org.jetbrains.annotations.NotNull;

/**
 * Comparable task priority. Default priorities: BEFORE (1.0), DEFAULT (2.0), AFTER (3.0)
 */
public final class GenerationTaskPriority implements Comparable<GenerationTaskPriority> {

    public static final GenerationTaskPriority BEFORE = new GenerationTaskPriority(1.0);
    public static final GenerationTaskPriority DEFAULT = new GenerationTaskPriority(2.0);
    public static final GenerationTaskPriority AFTER = new GenerationTaskPriority(3.0);

    private final double priority;

    public GenerationTaskPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull GenerationTaskPriority to) {
        return Double.compare(to.getPriority(), priority);
    }

    /**
     * Get the priority
     *
     * @return the priority
     */
    public double getPriority() {
        return priority;
    }

}
