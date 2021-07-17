/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.utilities.console;

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Test

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CompileStatic
final class ColoredTest {

    @Test
    void toString__text_null__without_effect() {
        assertEquals MessageFormat.format("null{0}", Effect.RESET),  Colored.on(null).toString()
    }

    @Test
    void toString__text_NOT_null__with_effects() {
        Colored colored = Colored.on("line1" + System.lineSeparator() + "line2")
        colored.effect(Effect.BLACK)
        colored.effect(Effect.BOLD)

        String expected = MessageFormat.format("{0}{1}line1{2}", Effect.BLACK, Effect.BOLD, Effect.RESET)
        expected += MessageFormat.format("{0}{1}line2{2}", Effect.BLACK, Effect.BOLD, Effect.RESET)

        assertEquals expected, colored.toString()
    }

}