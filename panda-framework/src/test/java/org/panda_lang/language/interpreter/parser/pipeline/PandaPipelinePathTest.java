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

package org.panda_lang.language.interpreter.parser.pipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.language.interpreter.parser.PandaContext;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.token.PandaTokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PandaPipelinePathTest {

    private static final PipelineComponent<?> TEST_COMPONENT = PipelineComponent.of("test", Parser.class);
    private static final PipelineComponent<?> ANOTHER_TEST_COMPONENT = PipelineComponent.of("another-test", Parser.class);

    private PipelinePath defaultPath;

    @BeforeEach
    void prepareDefaultPipelinePath() {
        this.defaultPath = new PandaPipelinePath();
        defaultPath.computeIfAbsent(TEST_COMPONENT);
    }

    @Test
    void createPipeline() {
        assertNotNull(defaultPath.computeIfAbsent(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void hasPipeline() {
        assertTrue(defaultPath.hasPipeline(TEST_COMPONENT));
        assertFalse(defaultPath.hasPipeline(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void getPipeline() {
        assertNotNull(defaultPath.getPipeline(TEST_COMPONENT));
        assertNull(defaultPath.getPipeline(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void getTotalHandleTime() {
        assertTrue(defaultPath.getPipeline(TEST_COMPONENT)
                .handle(new PandaContext(), new PandaLocalChannel(), PandaTokenInfo.of(TokenTypes.UNKNOWN, "test").toSnippet())
                .isErr());

        assertTrue(defaultPath.getTotalHandleTime() > 0);
    }

    @Test
    void names() {
        assertTrue(Arrays.asList("all", "test").containsAll(defaultPath.names()));
    }

}