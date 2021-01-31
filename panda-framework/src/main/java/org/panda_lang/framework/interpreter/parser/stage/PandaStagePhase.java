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

package org.panda_lang.framework.interpreter.parser.stage;

public final class PandaStagePhase implements StagePhase {

    private final String name;
    private final StageManager stageManager;
    private StageLayer currentPhase;
    private StageLayer nextPhase;

    public PandaStagePhase(StageManager controller, String name) {
        this.name = name;
        this.stageManager = controller;
        this.currentPhase = new PandaStageLayer(this);
        this.nextPhase = new PandaStageLayer(this);
    }

    @Override
    public boolean execute() {
        while (currentPhase.callNextTask()) {
            if (stageManager.countTasksBefore(this) > 0) {
                return false;
            }
        }

        currentPhase = nextPhase;
        nextPhase = new PandaStageLayer(this);

        return true;
    }

    @Override
    public int countTasks() {
        return currentPhase.countTasks() + nextPhase.countTasks();
    }

    @Override
    public StageLayer currentLayer() {
        return currentPhase;
    }

    @Override
    public StageLayer nextLayer() {
        return nextPhase;
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
