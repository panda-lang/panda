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
import org.panda_lang.utilities.commons.collection.Lists

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class ResourcesIterableTest {

    @Test
    void next_should_move_to_the_next_element_and_return_it() {
        Collection<String> mutable = Lists.mutableOf("a")
        Collection<String> immutable = Collections.singletonList("c")
        ResourcesIterable<String> iterable = new ResourcesIterable<>(mutable, immutable)

        mutable.add("b")
        Iterator<String> iterator = iterable.iterator()

        assertTrue iterator.hasNext()
        assertEquals "a", iterator.next()
        assertEquals "b", iterator.next()
        assertEquals "c", iterator.next()
        assertFalse iterator.hasNext()
    }

    @Test
    void constructor_should_throw_exception_if_empty() {
        assertThrows IllegalArgumentException.class, () -> new ResourcesIterable()
    }

}
