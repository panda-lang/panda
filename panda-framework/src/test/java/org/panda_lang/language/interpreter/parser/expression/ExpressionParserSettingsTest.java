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

package org.panda_lang.language.interpreter.parser.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParserSettings.ExpressionParserSettingsBuilder;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParserSettings.SelectedMode;

import java.util.Collections;

class ExpressionParserSettingsTest {

    @Test
    void includeSelected() {
        Assertions.assertNull(init().build().getSelectedMode());
        Assertions.assertEquals(SelectedMode.EXCLUDE, init().excludeSelected().build().getSelectedMode());
        Assertions.assertEquals(SelectedMode.INCLUDE, init().includeSelected().build().getSelectedMode());
    }

    @Test
    void withSelectedSubparsers() {
        Assertions.assertEquals(Collections.singletonList("a"), init().withSelectedSubparsers("a").build().getSelectedSubparsers());
        Assertions.assertNotEquals(Collections.singletonList("a"), init().withSelectedSubparsers(Collections.emptyList()).build().getSelectedSubparsers());
    }

    @Test
    void isStandaloneOnly() {
        Assertions.assertTrue(init().onlyStandalone().build().isStandaloneOnly());
    }

    @Test
    void create() {
        ExpressionParserSettings settings = init().build();
        Assertions.assertNotNull(settings);
    }

    private ExpressionParserSettingsBuilder init() {
        return ExpressionParserSettings.create();
    }

}