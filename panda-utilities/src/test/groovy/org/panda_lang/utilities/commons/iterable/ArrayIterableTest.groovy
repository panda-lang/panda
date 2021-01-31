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

package org.panda_lang.utilities.commons.iterable

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class ArrayIterableTest {

    @Test
    void next_should_move_to_the_next_element_and_return_it() {
        ArrayIterable<String> arrayIterable = new ArrayIterable<>(new String[]{ "a", "b", "c" })
        Iterator<String> iterator = arrayIterable.iterator()

        assertTrue iterator.hasNext()
        assertEquals "a", iterator.next()
        assertEquals "b", iterator.next()
        assertEquals "c", iterator.next()
        assertFalse iterator.hasNext()
    }

}
