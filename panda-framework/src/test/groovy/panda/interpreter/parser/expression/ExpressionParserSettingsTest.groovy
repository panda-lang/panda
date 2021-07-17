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

package panda.interpreter.parser.expression

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class ExpressionParserSettingsTest {

    @Test
    void isStandaloneOnly() {
        assertTrue init().onlyStandalone().build().isStandaloneOnly()
    }

    @Test
    void create() {
        assertNotNull init().build()
    }

    private static ExpressionParserSettings.ExpressionParserSettingsBuilder init() {
        return ExpressionParserSettings.create()
    }

}