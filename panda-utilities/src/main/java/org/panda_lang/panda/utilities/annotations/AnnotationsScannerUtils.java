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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerUtils {

    protected static final String[] PANDA_PACKAGES = {
            "META-INF",
            "java", "com.sun", "sun", "jdk", "javax", "oracle", "com.oracle", "netscape",       // Java
            "org.apache", "com.google", "org.slf4j",                                            // Popular
            "org.junit", "junit", "org.opentest4j",                                            // Tests
            "org.jetbrains", "org.intellij", "com.intellij",                                    // IDE
            "javassist", "org.fusesource", "org.apiguardian"                                    // Internal
    };

    static List<String> primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
    static List<String> primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
    static List<Class> primitiveTypes = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);

    public static String toClassPath(String path) {
        return path.replace("/", ".").replace(".class", "");
    }

    static Set<Class<?>> forNames(AnnotationsScanner scanner, Collection<String> types) {
        Set<Class<?>> classes = new HashSet<>();

        for (String type : types) {
            Class<?> clazz = forName(scanner, type);

            if (type == null) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

    static @Nullable Class<?> forName(AnnotationsScanner scanner, String typeName, @Nullable ClassLoader... classLoaders) {
        if (primitiveNames.contains(typeName)) {
            return primitiveTypes.get(primitiveNames.indexOf(typeName));
        }

        String type;

        if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            type = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");

            if (primitiveNames.contains(type)) {
                type = primitiveDescriptors.get(primitiveNames.indexOf(type));
            }
            else {
                type = "L" + type + ";";
            }

            type = array + type;
        }
        else {
            type = typeName;
        }

        for (ClassLoader classLoader : scanner.getConfiguration().getClassLoaders()) {
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                } catch (Throwable e) {
                    scanner.getLogger().exception(e);
                }
            }
            try {
                return classLoader.loadClass(type);
            } catch (Throwable e) {
                scanner.getLogger().exception(e);
            }
        }

        scanner.getLogger().warn("Could not get type for name " + typeName + " from any class loader");
        return null;
    }

}
