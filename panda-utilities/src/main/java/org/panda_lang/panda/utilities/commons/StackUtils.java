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

package org.panda_lang.panda.utilities.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackUtils {

    /**
     * Push multiple values at once
     *
     * @param stack  the stack to fill
     * @param values the values to push
     * @param <T>    the type of the stack content
     * @return the stack
     */
    @SafeVarargs
    public static <T> Stack<T> push(Stack<T> stack, T... values) {
        Arrays.stream(values).forEach(stack::push);
        return stack;
    }

    /**
     * Pop multiple values at once without worries that the stack is empty
     *
     * @param stack  the stack to drain
     * @param amount amount of values to pop
     * @param <T>    the type of the values
     * @return the stack
     */
    public static <T> Stack<T> popSilently(Stack<T> stack, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }

        return stack;
    }

    /**
     * Fill the stack with the specified object
     *
     * @param stack  the stack to fill
     * @param object the object to push
     * @param amount amount of pushes
     * @param <T>    the type of the values
     * @return the stack
     */
    public static <T> Stack<T> fill(Stack<T> stack, T object, int amount) {
        for (int i = 0; i < amount; i++) {
            stack.push(object);
        }

        return stack;
    }

    /**
     * Reverse the stack
     *
     * @param stack the stack to reverse
     * @param <T>   the type of the values
     * @return the stack with reversed values (the same instance)
     */
    public static <T> Stack<T> reverse(Stack<T> stack) {
        List<T> reversed = new ArrayList<>(stack);

        Collections.reverse(reversed);
        stack.clear();

        for (T element : reversed) {
            stack.push(element);
        }

        return stack;
    }

    /**
     * Create stack using another collection
     *
     * @param collection the collection to use
     * @param <T> type of collection
     * @return a new stack based on the specified collection
     */
    public static <T> Stack<T> of(Collection<? extends T> collection) {
        Stack<T> stack = new Stack<>();
        stack.addAll(collection);
        return stack;
    }

}
