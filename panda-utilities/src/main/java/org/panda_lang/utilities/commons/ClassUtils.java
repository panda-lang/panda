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

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ClassUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_EQUIVALENT = new HashMap<Class<?>, Class<?>>() {{
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);

        put(float.class, Float.class);
        put(double.class, Double.class);

        put(boolean.class, Boolean.class);
        put(char.class, Character.class);
    }};

    private ClassUtils() { }

    /**
     * Get classes of the given values
     *
     * @param values the values to check
     * @return array of classes
     */
    public static Class<?>[] getClasses(Object... values) {
        //noinspection ReturnOfNull
        return Arrays.stream(values)
                .map(object -> object != null ? object.getClass() : null)
                .toArray(Class[]::new);
    }

    /**
     * Select the most related class from the specified collection
     *
     * @param classes the collection to search in
     * @param to the class to compare with
     * @return the most related class from collection (may not exist)
     */
    public static Optional<Class<?>> selectMostRelated(Collection<? extends Class<?>> classes, Class<?> to) {
        Class<?> selected = null;

        for (Class<?> clazz : classes) {
            if (clazz.isAssignableFrom(to)) {
                if (selected != null && clazz.isAssignableFrom(selected)) {
                    continue;
                }

                selected = clazz;
            }
        }

        return Optional.ofNullable(selected);
    }

    /**
     * Check if the class is assignable to another with primitive types support.
     * Utility command for nullable fromObject.
     *
     * @see org.panda_lang.utilities.commons.ClassUtils#isAssignableFrom(Class, Class)
     */
    public static boolean isAssignableFrom(Class<?> toClass, @Nullable Object fromObject) {
        return fromObject != null && isAssignableFrom(toClass, fromObject.getClass());
    }

    /**
     * Check if the class is assignable to another with primitive types support
     *
     * @param toClass the root class
     * @param fromClass the {@link java.lang.Class} object to be checked
     * @return true if toClass is assignable to fromClass
     */
    public static boolean isAssignableFrom(Class<?> toClass, Class<?> fromClass) {
        if (toClass == null || fromClass == null) {
            return false;
        }

        if (toClass.isPrimitive()) {
            toClass = PRIMITIVE_EQUIVALENT.get(toClass);
        }

        if (fromClass.isPrimitive()) {
            fromClass = PRIMITIVE_EQUIVALENT.get(fromClass);
        }

        return toClass.isAssignableFrom(fromClass);

    }

    /**
     * Check if the class exists using {@link org.panda_lang.utilities.commons.ClassUtils#forName(String)}
     *
     * @param name the name of class
     * @return true if class exists, otherwise false
     */
    public static boolean exists(String name) {
        return forName(name).isPresent();
    }

    /**
     * Get class using {@link java.lang.Class#forName(String)} as {@link java.util.Optional}
     *
     * @param name the name of class
     * @return optional with class (with null instead of {@link java.lang.ClassNotFoundException})
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<? extends T>> forName(String name) {
        try {
            Class<T> clazz = (Class<T>) Class.forName(name);
            return Optional.of(clazz);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

}