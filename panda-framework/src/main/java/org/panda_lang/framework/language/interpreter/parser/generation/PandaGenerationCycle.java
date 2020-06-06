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

package org.panda_lang.framework.language.interpreter.parser.generation;

import org.panda_lang.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationCycle;
import org.panda_lang.framework.design.interpreter.parser.generation.GenerationPhase;

public final class PandaGenerationCycle implements GenerationCycle {

    private final String name;
    private final Generation generation;
    private GenerationPhase currentPhase;
    private GenerationPhase nextPhase;

    public PandaGenerationCycle(Generation generation, String name) {
        this.name = name;
        this.generation = generation;
        this.currentPhase = new PandaGenerationPhase(this);
        this.nextPhase = new PandaGenerationPhase(this);
    }

    @Override
    public boolean execute() throws Exception {
        while (true) {
            currentPhase.callTasks();

            if (currentPhase.countTasks() > 0) {
                continue;
            }

            if (nextPhase.countTasks() == 0) {
                break;
            }

            currentPhase = nextPhase;
            nextPhase = new PandaGenerationPhase(this);

            if (generation.countTasks(this) > 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int countTasks() {
        return currentPhase.countTasks() + nextPhase.countTasks();
    }

    @Override
    public GenerationPhase currentPhase() {
        return currentPhase;
    }

    @Override
    public GenerationPhase nextPhase() {
        return nextPhase;
    }

    @Override
    public Generation generation() {
        return generation;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "GenerationCycle#" + name + " { currentPhase: " + currentPhase + ", nextPhase: " + nextPhase + " }";
    }

}
