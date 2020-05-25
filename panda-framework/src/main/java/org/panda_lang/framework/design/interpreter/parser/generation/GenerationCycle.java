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

package org.panda_lang.framework.design.interpreter.parser.generation;

/**
 * Cycle represents following continuously pair of phases identified by the specific {@link org.panda_lang.framework.design.interpreter.parser.generation.CycleType}
 */
public interface GenerationCycle {

    /**
     * Launch cycle
     *
     * @return true if all tasks was called
     * @throws Exception allows you to handle exception that may occur in tasks
     */
    boolean execute() throws Exception;

    /**
     * @return amount of tasks
     */
    int countTasks();

    /**
     * Get next phase
     *
     * @return the next phase
     */
    GenerationPhase nextPhase();

    /**
     * Get current phase
     *
     * @return the current phase
     */
    GenerationPhase currentPhase();

    /**
     * Get generation
     *
     * @return the generation
     */
    Generation generation();

    /**
     * Get name of cycle
     *
     * @return the name
     */
    String name();

}
