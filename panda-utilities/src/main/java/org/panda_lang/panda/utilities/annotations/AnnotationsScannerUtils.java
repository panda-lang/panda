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

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import org.reflections.util.ClasspathHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerUtils {

    private static List<String> primitiveNames = Lists.newArrayList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");
    private static List<String> primitiveDescriptors = Lists.newArrayList("Z", "C", "B", "S", "I", "J", "F", "D", "V");
    private static List<Class> primitiveTypes = Lists.newArrayList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);

    protected static Set<Class<?>> forNames(Collection<String> types) {
        Set<Class<?>> classes = new HashSet<>();

        for (String type : types) {
            Class<?> clazz = forName(type);

            if (type == null) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

    protected static @Nullable Class<?> forName(String typeName, @Nullable ClassLoader... classLoaders) {
        if (getPrimitiveNames().contains(typeName)) {
            return getPrimitiveTypes().get(getPrimitiveNames().indexOf(typeName));
        }

        String type;

        if (typeName.contains("[")) {
            int i = typeName.indexOf("[");
            type = typeName.substring(0, i);
            String array = typeName.substring(i).replace("]", "");

            if (getPrimitiveNames().contains(type)) {
                type = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
            }
            else {
                type = "L" + type + ";";
            }

            type = array + type;
        }
        else {
            type = typeName;
        }

        for (ClassLoader classLoader : ClasspathHelper.classLoaders(classLoaders)) {
            if (type.contains("[")) {
                try {
                    return Class.forName(type, false, classLoader);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            try {
                return classLoader.loadClass(type);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        System.out.println("could not get type for name " + typeName + " from any class loader");
        return null;
    }

    protected static List<String> getPrimitiveNames() {
        return primitiveNames;
    }

    protected static List<Class> getPrimitiveTypes() {
        return primitiveTypes;
    }

    protected static List<String> getPrimitiveDescriptors() {
        return primitiveDescriptors;
    }

}
