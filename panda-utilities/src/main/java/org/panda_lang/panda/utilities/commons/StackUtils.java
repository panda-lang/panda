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
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackUtils {

    public static String getCallerClass(StackTraceElement[] stackTraceElements, Class<?> currentClass) {
        for (int i = 1; i < stackTraceElements.length; i++) {
            StackTraceElement element = stackTraceElements[i];

            if (element.getClassName().equals(currentClass.getName())) {
                continue;
            }

            return element.getClassName();
        }

        return null;
    }

    public static <T> Stack<T> popSilently(Stack<T> stack, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }

        return stack;
    }

    public static <T> Stack<T> fill(Stack<T> stack, T object, int amount) {
        for (int i = 0; i < amount; i++) {
            stack.push(object);
        }

        return stack;
    }

    public static <T> Stack<T> reverse(Stack<T> stack) {
        List<T> reversed = new ArrayList<>(stack);

        Collections.reverse(reversed);
        stack.clear();

        for (T element : reversed) {
            stack.push(element);
        }

        return stack;
    }

}
