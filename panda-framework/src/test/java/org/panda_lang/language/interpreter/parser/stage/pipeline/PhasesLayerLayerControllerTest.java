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

package org.panda_lang.language.interpreter.parser.stage.pipeline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.panda_lang.language.interpreter.parser.stage.Phase;
import org.panda_lang.language.interpreter.parser.stage.PandaStageManager;

import java.util.Arrays;

class PhasesLayerLayerControllerTest {

    private static final PandaStageManager STAGE_CONTROLLER = new PandaStageManager();

    @BeforeAll
    public static void createPipelines() {
        STAGE_CONTROLLER.initialize(Arrays.asList(
                new Phase("a", 1.0),
                new Phase("b", 2.0),
                new Phase("c", 3.0))
        );
    }

    @Test
    public void testPipelineGeneration() {
        StringBuilder outputBuilder = new StringBuilder();

        STAGE_CONTROLLER.getPhase("b").nextLayer().delegate("b", pipeline -> outputBuilder.append("b "));
        STAGE_CONTROLLER.getPhase("a").nextLayer().delegate("a", pipeline -> outputBuilder.append("a "));
        STAGE_CONTROLLER.getPhase("c").nextLayer().delegate("c", pipeline -> outputBuilder.append("c "));

        STAGE_CONTROLLER.getPhase("b").nextLayer().delegate("b2", pipeline -> {
            outputBuilder.append("b2 ");

            pipeline.nextLayer().delegate("a", phase -> {
                STAGE_CONTROLLER.getPhase("a").nextLayer().delegate("a2", pipeline2 -> outputBuilder.append("a2 "));
                outputBuilder.append("b3 ");
            });
        });

        STAGE_CONTROLLER.launch(() -> {});
        Assertions.assertEquals("a b b2 b3 a2 c", outputBuilder.toString().trim());

        outputBuilder.setLength(0);
        STAGE_CONTROLLER.launch(() -> {});
        Assertions.assertEquals("", outputBuilder.toString());
    }

}
