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

package org.panda_lang.utilities.commons.collection

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class FixedStackTest {

    @Test
    void push() {
        def stack = new FixedStack<>(1)
        stack.push("element")

        assertEquals "element", stack.peek()
        assertEquals "element", stack.pop()
    }

    @Test
    void peek() {
        def stack = new FixedStack<>(2)
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> stack.peek())

        stack.push("1")
        assertEquals "1", stack.peek()

        stack.push("2")
        assertEquals "2", stack.peek()

        stack.pop()
        assertEquals "1", stack.peek()
    }

    @Test
    void pop() {
        def stack = new FixedStack<>(2)
        assertThrows ArrayIndexOutOfBoundsException.class, () -> stack.pop()

        stack.push("1")
        stack.push("2")
        assertEquals "2", stack.pop()
        assertEquals "1", stack.pop()
    }

    @Test
    void size() {
        FixedStack<String> stack = new FixedStack<>(2);
        assertEquals 0, stack.size()

        stack.push("element")
        assertEquals 1, stack.size()

        stack.pop()
        assertEquals 0, stack.size()
    }

    @Test
    void clear() {
        FixedStack<String> stack = new FixedStack<>(2)
        stack.clear()
        stack.push("1")
        stack.push("2")
        stack.clear()

        assertTrue stack.isEmpty()
        assertThrows IndexOutOfBoundsException.class, { stack.peek() }
        assertEquals "[]", Arrays.toString(stack.toArray(String[].class))
    }

    @Test
    void isEmpty() {
        def stack = new FixedStack<>(1)
        assertTrue(stack.isEmpty())

        stack.push("element")
        assertFalse(stack.isEmpty())
    }

    @Test
    void toArray() {
        FixedStack<String> stack = new FixedStack<>(2)
        assertEquals("[]", Arrays.toString(stack.toArray(String[].class)))

        stack.push("1")
        stack.push("2")
        assertEquals("[2, 1]", Arrays.toString(stack.toArray(String[].class)))
    }

}