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

package org.panda_lang.language.interpreter.parser.stage;

public final class PandaStage implements Stage {

    private final String name;
    private final StageController stageController;
    private StagePhase currentPhase;
    private StagePhase nextPhase;

    public PandaStage(StageController controller, String name) {
        this.name = name;
        this.stageController = controller;
        this.currentPhase = new PandaStagePhase(this);
        this.nextPhase = new PandaStagePhase(this);
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
            nextPhase = new PandaStagePhase(this);

            if (stageController.countTasks(this) > 0) {
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
    public StagePhase currentPhase() {
        return currentPhase;
    }

    @Override
    public StagePhase nextPhase() {
        return nextPhase;
    }

    @Override
    public StageController stage() {
        return stageController;
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
