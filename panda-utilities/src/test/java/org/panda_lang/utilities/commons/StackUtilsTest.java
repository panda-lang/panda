/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Stack;

final class StackUtilsTest {

    @Test
    void push() {
        Stack<String> stack = StackUtils.push(new Stack<>(), "a", "b", "c");

        Assertions.assertEquals(3, stack.size());
        Assertions.assertEquals("c", stack.pop());
    }

    @Test
    void fill() {
        Assertions.assertEquals(9, StackUtils.fill(new Stack<>(), new Object(), 9).size());
    }

    @Test
    void popSilently() {
        Stack<?> stack = StackUtils.fill(new Stack<>(), new Object(), 10);
        Assertions.assertEquals(5, StackUtils.popSilently(stack, 5).size());
        Assertions.assertEquals(0, StackUtils.popSilently(stack, 6).size());
    }

    @Test
    void reverse() {
        Stack<String> stack = StackUtils.push(new Stack<>(), "a", "b", "c");
        Stack<String> reversed = StackUtils.reverse(stack);

        Assertions.assertSame(stack, reversed);
        Assertions.assertEquals(3, stack.size());
        Assertions.assertEquals("a", stack.pop());
    }

}
