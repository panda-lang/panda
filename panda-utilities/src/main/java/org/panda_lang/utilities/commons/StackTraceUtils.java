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

package org.panda_lang.utilities.commons;

import panda.std.stream.PandaStream;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.Predicate;

public final class StackTraceUtils {

    private StackTraceUtils() { }

    public static StackTraceElement[] startsWith(StackTraceElement[] stackTrace, Predicate<StackTraceElement> condition) {
        return PandaStream.of(stackTrace)
                .takeWhile(element -> !condition.test(element))
                .toArray(StackTraceElement[]::new);
    }

    public static StackTraceElement[] filter(StackTraceElement[] stackTrace, Class<?>... ignored) {
        return filter(stackTrace,  Arrays.stream(ignored)
                .map(Class::getName)
                .toArray(String[]::new)
        );
    }

    public static StackTraceElement[] filter(StackTraceElement[] stacktrace, String... ignored) {
        return filter(stacktrace, element -> Arrays.stream(ignored).anyMatch(ignoredName -> element.getClassName().startsWith(ignoredName)));
    }

    private static StackTraceElement[] filter(StackTraceElement[] stacktrace, Predicate<StackTraceElement> filter) {
        Stack<StackTraceElement> filtered = new Stack<>();

        for (StackTraceElement element : stacktrace) {
            if (!filter.test(element)) {
                filtered.push(element);
            }
        }

        return filtered.toArray(new StackTraceElement[0]);
    }

}
