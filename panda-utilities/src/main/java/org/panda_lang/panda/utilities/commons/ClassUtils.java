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

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassUtils {

    /**
     * Compare which class is assignable to another, higher priority has the uppermost class
     */
    public static final Comparator<Class<?>> CLASS_ASSIGNATION_COMPARATOR = (a, b) -> {
        if (Objects.equals(a, b)) {
            return 0;
        }

        if (a == null || isAssignableFrom(a, b)) {
            return -1;
        }

        if (b == null || isAssignableFrom(b, a)) {
            return 1;
        }

        return 0;
    };

    private static final Map<Class<?>, Class<?>> PRIMITIVE_EQUIVALENT = new HashMap<>();

    static {
        PRIMITIVE_EQUIVALENT.put(byte.class, Byte.class);
        PRIMITIVE_EQUIVALENT.put(short.class, Short.class);
        PRIMITIVE_EQUIVALENT.put(int.class, Integer.class);
        PRIMITIVE_EQUIVALENT.put(long.class, Long.class);

        PRIMITIVE_EQUIVALENT.put(double.class, Double.class);
        PRIMITIVE_EQUIVALENT.put(float.class, Float.class);

        PRIMITIVE_EQUIVALENT.put(boolean.class, Boolean.class);
        PRIMITIVE_EQUIVALENT.put(char.class, Character.class);
    }

    public static boolean isAssignableFrom(Class<?> to, @Nullable Object from) {
        if (from == null) {
            return false;
        }

        return isAssignableFrom(to, from.getClass());
    }

    public static boolean isAssignableFrom(Class<?> to, Class<?> from) {
        if (to == null || from == null) {
            return false;
        }

        if (to.isPrimitive()) {
            to = PRIMITIVE_EQUIVALENT.get(to);
        }

        if (from.isPrimitive()) {
            from = PRIMITIVE_EQUIVALENT.get(from);
        }

        return to.isAssignableFrom(from);

    }

    public static boolean exists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}