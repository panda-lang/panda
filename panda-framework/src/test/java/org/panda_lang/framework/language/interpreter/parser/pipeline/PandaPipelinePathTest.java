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

package org.panda_lang.framework.language.interpreter.parser.pipeline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.framework.language.interpreter.token.PandaTokenInfo;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;

import java.util.Arrays;

class PandaPipelinePathTest {

    private static final PipelineComponent<?> TEST_COMPONENT = PipelineComponent.of("test", Parser.class);
    private static final PipelineComponent<?> ANOTHER_TEST_COMPONENT = PipelineComponent.of("another-test", Parser.class);

    private PipelinePath defaultPath;

    @BeforeEach
    void prepareDefaultPipelinePath() {
        this.defaultPath = new PandaPipelinePath();
        defaultPath.createPipeline(TEST_COMPONENT);
    }

    @Test
    void createPipeline() {
        Assertions.assertNotNull(defaultPath.createPipeline(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void hasPipeline() {
        Assertions.assertTrue(defaultPath.hasPipeline(TEST_COMPONENT));
        Assertions.assertFalse(defaultPath.hasPipeline(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void getPipeline() {
        Assertions.assertNotNull(defaultPath.getPipeline(TEST_COMPONENT));
        Assertions.assertNull(defaultPath.getPipeline(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void getTotalHandleTime() {
        Assertions.assertNull(defaultPath.getPipeline(TEST_COMPONENT)
                .handle(new PandaContext(), new PandaLocalChannel(), PandaTokenInfo.of(TokenTypes.UNKNOWN, "test").toSnippet())
                .getParser().getOrNull());

        Assertions.assertTrue(defaultPath.getTotalHandleTime() > 0);
    }

    @Test
    void names() {
        Assertions.assertTrue(Arrays.asList("all", "test").containsAll(defaultPath.names()));
    }

}