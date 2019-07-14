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

package org.panda_lang.panda.utilities.autodata.data.entity;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class EntitySchemeLoader {

    private static final String CAMEL_CASE_PATTERN = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    protected EntityScheme load(Class<?> entityClass) {
        if (!entityClass.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + entityClass.toGenericString() + ")");
        }

        Map<String, EntitySchemeProperty> properties = new HashMap<>();

        for (Method method : entityClass.getDeclaredMethods()) {
            EntitySchemeProperty property = load(method);

            if (properties.containsKey(property.getName())) {
                EntitySchemeProperty cachedProperty = properties.get(property.getName());

                if (cachedProperty.getType().equals(property.getType())) {
                    continue;
                }

                throw new AutomatedDataException("Methods associated with the same property cannot have different return type (" + method + " != " + cachedProperty.getAssociatedMethod() + ")");
            }

            properties.put(property.getName(), property);
        }

        return new EntityScheme(entityClass, properties);
    }

    private EntitySchemeProperty load(Method method) {
        List<String> elements = Arrays.stream(method.getName().split(CAMEL_CASE_PATTERN))
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        String propertyName = ContentJoiner.on("_").join(elements.subList(1, elements.size( ))).toString();
        Class<?> type = getType(elements.get(0), method);

        if (type == null) {
            throw new AutomatedDataException("Unknown operation '" + elements.get(0) + "'");
        }

        return new EntitySchemeProperty(propertyName, type, method);
    }

    private @Nullable Class<?> getType(String operation, Method method) {
        switch (operation) {
            case "get":
                return method.getReturnType();
            case "set":
                return method.getParameterTypes()[0];
            default:
                return null;
        }
    }

}
