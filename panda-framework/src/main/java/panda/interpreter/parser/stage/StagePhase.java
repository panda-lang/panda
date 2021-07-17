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

package panda.interpreter.parser.stage;

/**
 * Cycle represents following continuously pair of phases identified by the specific {@link Phase}
 */
public interface StagePhase {

    /**
     * Launch cycle
     *
     * @return true if all tasks was called
     */
    boolean execute();

    /**
     * Get next phase
     *
     * @return the next phase
     */
    StageLayer nextLayer();

    /**
     * Get current phase
     *
     * @return the current phase
     */
    StageLayer currentLayer();

    /**
     * @return amount of tasks
     */
    int countTasks();

    /**
     * Get name of cycle
     *
     * @return the name
     */
    String name();

}
