/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.commons.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

final class SectionStringTest {

    private static final String CONTENT = "Test|Bracket(|)|String'|'|Test";
    private static final char SEPARATOR = '|';

    @Test
    void split() {
        SectionString sectionString = SectionString.of(CONTENT).build();
        List<String> selected = sectionString.split(SEPARATOR);

        Assertions.assertNotNull(selected);
        Assertions.assertEquals(4, selected.size());

        Assertions.assertAll(
                () -> Assertions.assertEquals("Test", selected.get(0)),
                () -> Assertions.assertEquals("Bracket(|)", selected.get(1)),
                () -> Assertions.assertEquals("String'|'", selected.get(2)),
                () -> Assertions.assertEquals("Test", selected.get(3))
        );
    }

}
