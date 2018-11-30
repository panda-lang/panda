/*
 * Copyright (c) 2015-2018 Dzikoysk
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReflectionUtils {

    /**
     * Collect methods annotated with the specified annotation
     *
     * @param clazz class to search
     * @param annotation annotation to search for
     * @return the list of annotated methods
     */
    public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        Class<?> currentClazz = clazz;

        while (currentClazz != Object.class) {
            Method[] declaredMethods = currentClazz.getDeclaredMethods();

            for (Method method : declaredMethods) {
                if (!method.isAnnotationPresent(annotation)) {
                   continue;
                }

                Annotation annotationInstance = method.getAnnotation(annotation);
                methods.add(method);
            }

            currentClazz = currentClazz.getSuperclass();
        }

        return methods;
    }

    /**
     * Collect all methods with the same name
     *
     * @param clazz class to search
     * @param methodName name to search for
     * @return the list of the matching methods
     */
    public static List<Method> getMethods(Class<?> clazz, String methodName) {
        return CollectionUtils.listOf(getMethods(clazz.getMethods(), methodName), getMethods(clazz.getDeclaredMethods(), methodName));
    }

    /**
     * Collect all methods with same name
     *
     * @param methods methods to search
     * @param methodName name to search for
     * @return the list of the matching methods
     */
    public static List<Method> getMethods(Method[] methods, String methodName) {
        List<Method> matchedMethods = new ArrayList<>();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                matchedMethods.add(method);
            }
        }

        return matchedMethods;
    }

    public static <T> Collection<T> getStaticFieldValues(Class<?> clazz, Class<T> type) {
        return getFieldValues(clazz, type, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getFieldValues(Class<?> clazz, Class<T> type, @Nullable T instance) {
        Collection<Field> fields = new ArrayList<>(type.getDeclaredFields().length);

        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getType() != type) {
                continue;
            }

            if (instance == null && !Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            fields.add(declaredField);
        }

        Collection<T> values = new ArrayList<>(fields.size());

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(instance);
                values.add(value != null ? (T) value : null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return values;
    }

}
