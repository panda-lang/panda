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

import org.jetbrains.annotations.Nullable;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() { }

    /**
     * Get executables with the specified modifier
     *
     * @param executables array of executables to search in
     * @param modifier the modifier to search for
     * @param <E> type of executable
     * @return collection of matched executables
     */
    public static <E extends Executable> Collection<E> getByModifier(E[] executables, int modifier) {
        return Arrays.stream(executables)
                .filter(executable -> (executable.getModifiers() & modifier) != 0)
                .collect(Collectors.toList());
    }

    /**
     * Get method wrapped into optional instead of exception if method does not exist.
     * Comparing to {@link java.lang.Class#getMethod(String, Class[])} it also checks non-public methods.
     *
     * @param clazz the class to search in
     * @param methodName name of method
     * @param parameterTypes parameter types of method
     * @return the result method wrapped into optional
     */
    public static Option<Method> getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return PandaStream.of(getMethods(clazz, methodName))
                .filter(method -> Arrays.equals(method.getParameterTypes(), parameterTypes))
                .head();
    }

    /**
     * Collect all methods with the same name
     *
     * @param clazz      class to search
     * @param methodName name to search for
     * @return the list of the matching methods
     */
    public static List<Method> getMethods(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName))
                .collect(Collectors.toList());
    }

    /**
     * Collect methods annotated with the specified annotation
     *
     * @param clazz      class to search
     * @param annotation annotation to search for
     * @return the set of annotated methods
     */
    public static Set<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        Set<Method> methods = new HashSet<>();
        Class<?> currentClazz = clazz;

        while (currentClazz != Object.class) {
            methods.addAll(Arrays.stream(currentClazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(annotation))
                    .collect(Collectors.toList()));

            currentClazz = currentClazz.getSuperclass();
        }

        return methods;
    }

    /**
     * Collect values of static fields with the specified type
     *
     * @param clazz class to search in
     * @param type type of field
     * @param <T> value type
     * @return collection of collected values
     */
    public static <T> Collection<T> getStaticFieldValues(Class<?> clazz, Class<T> type) {
        return getFieldValues(clazz, type, null);
    }

    /**
     * Collect values of static fields with the specified type
     *
     * @param clazz class to search in
     * @param type type of fields
     * @param instance instance
     * @param <R> type of values
     * @param <T> type of instance
     * @return collection of collected values
     */
    @SuppressWarnings("unchecked")
    public static <R, T> Collection<R> getFieldValues(Class<T> clazz, Class<R> type, @Nullable T instance) {
        Collection<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getType() == type)
                .filter(field -> instance != null || Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());

        Collection<R> values = new ArrayList<>(fields.size());

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                values.add((R) field.get(instance));
            }
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException("Cannot access field", illegalAccessException);
        }

        return values;
    }

}
