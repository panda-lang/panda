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

package org.panda_lang.panda.utilities.annotations.utils;

import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.collection.Sets;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MethodDescriptorUtils {

    public static boolean isConstructor(String descriptor) {
        return descriptor.contains("init>");
    }

    public static Member getMemberFromDescriptor(String descriptor, Collection<ClassLoader> classLoaders) {
        int left = descriptor.lastIndexOf('(');
        String memberKey = left != -1 ? descriptor.substring(0, left) : descriptor;
        String methodParameters = left != -1 ? descriptor.substring(left + 1, descriptor.lastIndexOf(')')) : StringUtils.EMPTY;

        int p1 = Math.max(memberKey.lastIndexOf('.'), memberKey.lastIndexOf("$"));
        String className = memberKey.substring(memberKey.lastIndexOf(' ') + 1, p1);
        String memberName = memberKey.substring(p1 + 1);

        Class<?>[] parameterTypes = null;

        if (!StringUtils.isEmpty(methodParameters)) {
            String[] parameterNames = StringUtils.split(methodParameters, ",");
            List<Class<?>> result = new ArrayList<>(parameterNames.length);

            for (String name : parameterNames) {
                result.add(AnnotationsScannerUtils.forName(name.trim(), classLoaders));
            }

            parameterTypes = result.toArray(new Class[0]);
        }

        Class<?> clazz = AnnotationsScannerUtils.forName(className, classLoaders);

        while (clazz != null) {
            try {
                if (!descriptor.contains("(")) {
                    return clazz.isInterface() ? clazz.getField(memberName) : clazz.getDeclaredField(memberName);
                }
                else if (isConstructor(descriptor)) {
                    return clazz.isInterface() ? clazz.getConstructor(parameterTypes) : clazz.getDeclaredConstructor(parameterTypes);
                }
                else {
                    return clazz.isInterface() ? clazz.getMethod(memberName, parameterTypes) : clazz.getDeclaredMethod(memberName, parameterTypes);
                }
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new RuntimeException("Can't resolve member named " + memberName + " for class " + className);
    }

    public static Set<Method> getMethodsFromDescriptors(Iterable<String> annotatedWith, Collection<ClassLoader> classLoaders) {
        Set<Method> result = Sets.newHashSet();

        for (String annotated : annotatedWith) {
            if (!isConstructor(annotated)) {
                Method member = (Method) getMemberFromDescriptor(annotated, classLoaders);

                if (member != null) {
                    result.add(member);
                }
            }
        }

        return result;
    }

    public static Set<Constructor> getConstructorsFromDescriptors(Iterable<String> annotatedWith, Collection<ClassLoader> classLoaders) {
        Set<Constructor> result = Sets.newHashSet();

        for (String annotated : annotatedWith) {
            if (isConstructor(annotated)) {
                Constructor member = (Constructor) getMemberFromDescriptor(annotated, classLoaders);

                if (member != null) {
                    result.add(member);
                }
            }
        }

        return result;
    }

    public static Set<Member> getMembersFromDescriptors(Iterable<String> values, Collection<ClassLoader> classLoaders) {
        Set<Member> result = Sets.newHashSet();

        for (String value : values) {
            result.add(getMemberFromDescriptor(value, classLoaders));
        }

        return result;
    }

    public static Field getFieldFromString(String field, Collection<ClassLoader> classLoaders) {
        String className = field.substring(0, field.lastIndexOf('.'));
        String fieldName = field.substring(field.lastIndexOf('.') + 1);

        try {
            //noinspection ConstantConditions
            return AnnotationsScannerUtils.forName(className, classLoaders).getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException("Can't resolve field named " + fieldName, e);
        }
    }

}
