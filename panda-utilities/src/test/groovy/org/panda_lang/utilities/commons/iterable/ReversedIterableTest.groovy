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
final class ReversedIterableTest {

    @Test
    void next_should_move_to_the_next_element_in_order_from_the_back_and_return_it() {
        ReversedIterable<String> reversedIterable = new ReversedIterable<>(Arrays.asList("a", "b", "c"))
        Iterator<String> iterator = reversedIterable.iterator()

        assertTrue iterator.hasNext()
        assertEquals "c", iterator.next()
        assertEquals "b", iterator.next()
        assertEquals "a", iterator.next()
        assertFalse iterator.hasNext()
    }

}
