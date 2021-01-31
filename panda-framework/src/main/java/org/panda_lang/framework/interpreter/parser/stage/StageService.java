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

import org.panda_lang.framework.interpreter.parser.PandaParserException;

public class StageService {

    private final StageManager stageManager;

    public StageService(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void delegate(String id, Phase phase, Layer layer, StageTask task) {
        StagePhase selectedPhase = stageManager.getPhase(phase);

        switch (layer) {
            case CURRENT_BEFORE:
                selectedPhase.currentLayer().delegateBefore(id, task);
                break;
            case CURRENT_DEFAULT:
                selectedPhase.currentLayer().delegate(id, task);
                break;
            case CURRENT_AFTER:
                selectedPhase.currentLayer().delegateAfter(id, task);
                break;
            case NEXT_BEFORE:
                selectedPhase.nextLayer().delegateBefore(id, task);
                break;
            case NEXT_DEFAULT:
                selectedPhase.nextLayer().delegate(id, task);
                break;
            case NEXT_AFTER:
                selectedPhase.nextLayer().delegateAfter(id, task);
                break;
            default:
                throw new PandaParserException("Unknown layer: " + layer);
        }
    }

}
