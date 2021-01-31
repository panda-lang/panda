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

package org.panda_lang.framework.interpreter.parser.stage

import groovy.transform.CompileStatic
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

@CompileStatic
final class PhasesLayerLayerControllerTest {

    private static final PandaStageManager STAGE_CONTROLLER = new PandaStageManager()

    @BeforeAll
    static void createPipelines() {
        STAGE_CONTROLLER.initialize([
                new Phase("a", 1.0D),
                new Phase("b", 2.0D),
                new Phase("c", 3.0D)
        ])
    }

    @Test
    void testPipelineGeneration() {
        def outputBuilder = new StringBuilder()

        STAGE_CONTROLLER.getPhase("b").nextLayer().delegate("b", { StagePhase phase -> outputBuilder.append("b ") })
        STAGE_CONTROLLER.getPhase("a").nextLayer().delegate("a", { StagePhase phase -> outputBuilder.append("a ") })
        STAGE_CONTROLLER.getPhase("c").nextLayer().delegate("c", { StagePhase phase -> outputBuilder.append("c ") })

        STAGE_CONTROLLER.getPhase("b").nextLayer().delegate("b2", { StagePhase phase ->
            outputBuilder.append("b2 ")

            phase.nextLayer().delegate("a", { StagePhase phase1 ->
                STAGE_CONTROLLER.getPhase("a").nextLayer().delegate("a2", { StagePhase phase2 -> outputBuilder.append("a2 ") })
                outputBuilder.append("b3 ")
            })
        })

        STAGE_CONTROLLER.launch(() -> {})
        assertEquals "a b b2 b3 a2 c", outputBuilder.toString().trim()

        outputBuilder.setLength(0)
        STAGE_CONTROLLER.launch(() -> {})
        assertEquals "", outputBuilder.toString()
    }

}
