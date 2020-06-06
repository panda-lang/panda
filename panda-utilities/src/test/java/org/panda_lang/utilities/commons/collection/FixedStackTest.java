/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.commons.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class FixedStackTest {

    @Test
    void push() {
        FixedStack<String> stack = new FixedStack<>(1);
        stack.push("element");

        Assertions.assertEquals("element", stack.peek());
        Assertions.assertEquals("element", stack.pop());
    }

    @Test
    void peek() {
        FixedStack<String> stack = new FixedStack<>(2);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, stack::peek);

        stack.push("1");
        Assertions.assertEquals("1", stack.peek());

        stack.push("2");
        Assertions.assertEquals("2", stack.peek());

        stack.pop();
        Assertions.assertEquals("1", stack.peek());
    }

    @Test
    void pop() {
        FixedStack<String> stack = new FixedStack<>(2);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, stack::pop);

        stack.push("1");
        stack.push("2");
        Assertions.assertEquals("2", stack.pop());
        Assertions.assertEquals("1", stack.pop());
    }

    @Test
    void size() {
        FixedStack<String> stack = new FixedStack<>(2);
        Assertions.assertEquals(0, stack.size());

        stack.push("element");
        Assertions.assertEquals(1, stack.size());

        stack.pop();
        Assertions.assertEquals(0, stack.size());
    }

    @Test
    void clear() {
        FixedStack<String> stack = new FixedStack<>(2);
        stack.clear();

        stack.push("1");
        stack.push("2");
        stack.clear();
        Assertions.assertTrue(stack.isEmpty());
        Assertions.assertThrows(IndexOutOfBoundsException.class, stack::peek);
        Assertions.assertEquals("[]", Arrays.toString(stack.toArray(String[].class)));
    }

    @Test
    void isEmpty() {
        FixedStack<String> stack = new FixedStack<>(1);
        Assertions.assertTrue(stack.isEmpty());

        stack.push("element");
        Assertions.assertFalse(stack.isEmpty());
    }

    @Test
    void toArray() {
        FixedStack<String> stack = new FixedStack<>(2);
        Assertions.assertEquals("[]", Arrays.toString(stack.toArray(String[].class)));

        stack.push("1");
        stack.push("2");
        Assertions.assertEquals("[2, 1]", Arrays.toString(stack.toArray(String[].class)));
    }

}