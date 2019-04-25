/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.design.interpreter.parser.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ExpressionParserSettingsTest {

    @Test
    void includeSelected() {
        Assertions.assertNull(ExpressionParserSettings.create().getSelectedMode());
        Assertions.assertTrue(ExpressionParserSettings.create().excludeSelected().getSelectedMode());
        Assertions.assertFalse(ExpressionParserSettings.create().includeSelected().getSelectedMode());
    }

    @Test
    void withSelectedSubparsers() {
        Assertions.assertEquals(Collections.singletonList("a"), ExpressionParserSettings.create().withSelectedSubparsers("a").getSelectedSubparsers());
        Assertions.assertNotEquals(Collections.singletonList("a"), ExpressionParserSettings.create().withSelectedSubparsers(Collections.emptyList()).getSelectedSubparsers());
    }

    @Test
    void isStandaloneOnly() {
        Assertions.assertTrue(ExpressionParserSettings.create().onlyStandalone().isStandaloneOnly());
    }

    @Test
    void isCombined() {
        Assertions.assertTrue(ExpressionParserSettings.create().withCombinedExpressions().isCombined());
    }

    @Test
    void create() {
        ExpressionParserSettings settings = ExpressionParserSettings.create();
        Assertions.assertNotNull(settings);
        Assertions.assertEquals(ExpressionParserSettings.DEFAULT, settings);
    }

}