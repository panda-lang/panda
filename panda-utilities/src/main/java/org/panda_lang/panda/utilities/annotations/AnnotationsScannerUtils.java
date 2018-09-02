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

package org.panda_lang.panda.utilities.annotations;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.utils.MethodDescriptorUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationsScannerUtils {

    public static String toClassPath(String path) {
        return path.replace("/", ".").replace(".class", "");
    }

    public static Set<Method> forMethods(AnnotationsScannerProcess process, Collection<String> descriptors) {
        return MethodDescriptorUtils.getMembersFromDescriptors(descriptors, process.getScanner().getConfiguration().getClassLoaders()).stream()
                .filter(member -> member instanceof Method)
                .map(member -> (Method) member)
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> forNames(AnnotationsScannerProcess process, Collection<String> types) {
        return forNames(process.getScanner(), types);
    }

    public static Set<Class<?>> forNames(AnnotationsScanner scanner, Collection<String> types) {
        Set<Class<?>> classes = new HashSet<>();

        for (String type : types) {
            if (type == null) {
                continue;
            }

            Class<?> clazz = forName(type, scanner.getConfiguration().getClassLoaders());

            if (clazz == null) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

    public static @Nullable Class<?> forName(String typeName, @Nullable Collection<ClassLoader> classLoaders) {
        if (AnnotationsScannerConstants.primitiveNames.contains(typeName)) {
            return AnnotationsScannerConstants.primitiveTypes.get(AnnotationsScannerConstants.primitiveNames.indexOf(typeName));
        }

        String type;

        if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            type = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");

            if (AnnotationsScannerConstants.primitiveNames.contains(type)) {
                type = AnnotationsScannerConstants.primitiveDescriptors.get(AnnotationsScannerConstants.primitiveNames.indexOf(type));
            }
            else {
                type = "L" + type + ";";
            }

            type = array + type;
        }
        else {
            type = typeName;
        }

        if (classLoaders == null) {
            return null;
        }

        for (ClassLoader classLoader : classLoaders) {
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            try {
                return classLoader.loadClass(type);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
