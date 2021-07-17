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

package panda.utilities.text

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class SectionStringTest {

    private static final String CONTENT = "Test|Bracket(|)|String'|'|Test"
    private static final char SEPARATOR = '|'

    @Test
    void split() {
        def sectionString = SectionString.of(CONTENT).build()
        List<String> selected = sectionString.split(SEPARATOR)

        assertNotNull selected
        assertEquals 4, selected.size()

        assertAll(
                () -> assertEquals "Test", selected.get(0),
                () -> assertEquals "Bracket(|)", selected.get(1),
                () -> assertEquals "String'|'", selected.get(2),
                () -> assertEquals "Test", selected.get(3)
        )
    }

}
