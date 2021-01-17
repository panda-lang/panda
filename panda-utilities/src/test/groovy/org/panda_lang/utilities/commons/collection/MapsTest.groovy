/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.commons.collection

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows

@CompileStatic
final class MapsTest {

    @Test
    void swapped() {
        Map<String, Integer> map = new HashMap<String, Integer>() {{
            put("a", 1)
            put("b", 2)
        }};

        Map<Integer, String> swapped = Maps.swapped(map, { new HashMap<Integer, String>() })
        assertEquals("a", swapped.get(1))
        assertEquals("b", swapped.get(2))
    }

    @Test
    void entryOf() {
        Map.Entry<String, Integer> entry = Maps.entryOf("a", 1)
        assertEquals("a", entry.getKey())
        assertEquals(1, entry.getValue())

        entry.setValue(2)
        assertEquals(2, entry.getValue())
    }

    @Test
    void immutableEntryOf() {
        Map.Entry<String, Integer> entry = Maps.immutableEntryOf("a", 1)
        assertEquals("a", entry.getKey())
        assertEquals(1, entry.getValue())
        assertThrows(UnsupportedOperationException.class, () -> entry.setValue(2))
    }

}