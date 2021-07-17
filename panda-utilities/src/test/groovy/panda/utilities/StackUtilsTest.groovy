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

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertSame

@CompileStatic
final class StackUtilsTest {

    @Test
    void push() {
        def stack = StackUtils.push(new Stack<>(), "a", "b", "c")

        assertEquals 3, stack.size()
        assertEquals "c", stack.pop()
    }

    @Test
    void fill() {
        assertEquals 9, StackUtils.fill(new Stack<>(), new Object(), 9).size()
    }

    @Test
    void popSilently() {
        def stack = StackUtils.fill(new Stack<>(), new Object(), 10)
        assertEquals 5, StackUtils.popSilently(stack, 5).size()
        assertEquals 0, StackUtils.popSilently(stack, 6).size()
    }

    @Test
    void reverse() {
        def stack = StackUtils.push(new Stack<>(), "a", "b", "c")
        def reversed = StackUtils.reverse(stack)

        assertSame stack, reversed
        assertEquals 3, stack.size()
        assertEquals "a", stack.pop()
    }

}
