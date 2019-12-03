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

/**
 * Comparable type of cycle, required to create a new cycle
 */
public final class CycleType implements Comparable<CycleType> {

    private final String name;
    private final double priority;

    /**
     * Create new cycle type
     *
     * @param name the name of cycle
     * @param priority the priority
     */
    public CycleType(String name, double priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int compareTo(CycleType to) {
        return Double.compare(priority, to.priority);
    }

    /**
     * Get priority of cycle
     *
     * @return the priority
     */
    public double getPriority() {
        return priority;
    }

    /**
     * Get name of cycle
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

}
