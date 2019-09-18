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

import java.util.Optional;

/**
 * Generation is the multilayer task manager divided into cycles and phases.
 */
public interface Generation {

    /**
     * Launch cycles
     *
     * @throws Exception if {@link org.panda_lang.framework.design.interpreter.parser.generation.GenerationPhase} throws an exception, you should catch it and handle
     */
    void launch() throws Exception;

    /**
     * Get amount of tasks before the specified cycle.
     *
     * @param to the cycle to which it count
     * @return the amount of tasks
     */
    int countTasks(GenerationCycle to);

    /**
     * Get amount of all tasks
     *
     * @return the amount of tasks
     */
    int countTasks();

    /**
     * @return current cycle
     */
    Optional<GenerationCycle> getCurrentCycle();

    /**
     * Get cycle
     *
     * @param name the name of cycle
     * @return the cycle
     */
    GenerationCycle getCycle(String name);

    /**
     * Get cycle
     *
     * @param type the type of cycle
     * @return the cycle
     */
    GenerationCycle getCycle(CycleType type);

}
