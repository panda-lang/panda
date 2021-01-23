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
import org.junit.jupiter.api.function.Executable

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class ListsTest {

    private static final List<String> LIST = Arrays.asList("a", "b", "c")

    @Test
    void get() {
        assertEquals(3, LIST.size())

        assertAll(
                () -> assertNotNull(Lists.get(LIST, 0)),
                () -> assertNotNull(Lists.get(LIST, 2)),

                () -> assertNull(Lists.get(LIST, -1)),
                () -> assertNull(Lists.get(LIST, 3))
        )
    }

    @Test
    void subList() {
        List<String> subList = Lists.subList(LIST, 1)
        assertEquals(Arrays.asList("b", "c"), subList)

        ArrayList<String> mutList = Lists.subList(LIST, 0, LIST.size())
        
        for (String element : LIST) {
            assertDoesNotThrow({ mutList.add(element) } as Executable)
        }
        
        assertEquals(Arrays.asList("a", "b", "c", "a", "b", "c"), mutList)
    }

    @Test
    void sort() {
        List<Integer> a = Arrays.asList(3, 2, 1)
        List<Integer> b = Arrays.asList(3, 2, 1)
        Lists.sort(Comparator.naturalOrder(), a, b)

        List<Integer> expected = Arrays.asList(1, 2, 3)
        assertEquals(expected, a)
        assertEquals(expected, b)
    }

    @Test
    void reverse() {
        assertEquals(Arrays.asList("c", "b", "a"), Lists.reverse(Lists.mutableOf("a", "b", "c")))
    }

    @Test
    void split() {
        List<String>[] lists = Lists.split(LIST, "b")
        assertEquals(2, lists.length)
        assertAll(
                () -> assertEquals(Collections.singletonList("a"), lists[0]),
                () -> assertEquals(Collections.singletonList("c"), lists[1])
        )
    }

    @Test
    void splitSingletonList() {
        def singletonList = Collections.singletonList('value')
        def splitSingletonList = Lists.split(singletonList, 'value')
        assertEquals 1, splitSingletonList.size()
        assertEquals singletonList, splitSingletonList[0]
    }

    @Test
    void mutableOf() {
        List<String> list = Lists.mutableOf("a", "b")
        list.add("c")

        assertEquals(Arrays.asList("a", "b", "c"), list)
    }

    @Test
    void add() {
        assertEquals("a", Lists.add(Lists.mutableOf(), "a"))
    }

}