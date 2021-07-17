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

package panda.utilities

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import panda.utilities.collection.Sets

import static org.junit.jupiter.api.Assertions.assertAll
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class CollectionUtilsTest {

    @Test
    void testListOf() {
        List<String> list = CollectionUtils.listOf(Sets.newHashSet("a"), Sets.newHashSet("b"))

        assertAll(
                () -> assertEquals(2, list.size()),
                () -> assertTrue(list.contains("a")),
                () -> assertTrue(list.contains("b"))
        )
    }

}
