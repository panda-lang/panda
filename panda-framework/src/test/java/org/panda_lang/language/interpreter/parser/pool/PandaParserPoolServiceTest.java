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

package org.panda_lang.language.interpreter.parser.pool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.utilities.commons.collection.Component;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PandaParserPoolServiceTest {

    private static final Component<Parser> TEST_COMPONENT = Component.of("test", Parser.class);
    private static final Component<Parser> ANOTHER_TEST_COMPONENT = Component.of("another-test", Parser.class);

    private ParserPoolService defaultPath;

    @BeforeEach
    void prepareDefaultPipelinePath() {
        this.defaultPath = new PandaParserPoolService();
        defaultPath.computeIfAbsent(TEST_COMPONENT);
    }

    @Test
    void createPipeline() {
        assertNotNull(defaultPath.computeIfAbsent(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void hasPipeline() {
        assertTrue(defaultPath.hasPool(TEST_COMPONENT));
        assertFalse(defaultPath.hasPool(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void getPipeline() {
        assertNotNull(defaultPath.getPool(TEST_COMPONENT));
        assertNull(defaultPath.getPool(ANOTHER_TEST_COMPONENT));
    }

    @Test
    void names() {
        assertTrue(Arrays.asList("all", "test").containsAll(defaultPath.names()));
    }

}