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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtils {

    public static List<Method> getMethods(Class<?> type, String name) {
        List<Method> methods = new ArrayList<>();

        for (Method method : type.getMethods()) {
            if (method.getName().equals(name)) {
                methods.add(method);
            }
        }

        for (Method declaredMethod : type.getDeclaredMethods()) {
            if (declaredMethod.getName().equals(name)) {
                methods.add(declaredMethod);
            }
        }

        return methods;
    }

    public static List<Method> getMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<Method>();
        Class<?> clazz = type;

        while (clazz != Object.class) {
            List<Method> allMethods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));

            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    Annotation annotInstance = method.getAnnotation(annotation);
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

}
