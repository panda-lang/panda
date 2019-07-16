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
import org.panda_lang.panda.utilities.commons.annotations.Annotations;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class EntitySchemeMethodLoader {

    private static final String CAMEL_CASE_PATTERN = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    protected EntitySchemeMethod load(Method method) {
        List<String> elements = Arrays.stream(method.getName().split(CAMEL_CASE_PATTERN))
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        String propertyName = ContentJoiner.on("_").join(elements.subList(1, elements.size())).toString();
        EntitySchemeOperationType operationType = EntitySchemeOperationType.of(elements.get(0));

        if (operationType == null) {
            throw new AutomatedDataException("Unknown operation '" + elements.get(0) + "'");
        }

        EntitySchemeProperty property = new EntitySchemeProperty(propertyName, getType(operationType, method), new Annotations(method.getAnnotations()), method);
        return new EntitySchemeMethod(method, property, operationType);
    }

    private @Nullable Class<?> getType(EntitySchemeOperationType operationType, Method method) {
        switch (operationType) {
            case GET:
            case CREATE:
            case FIND:
                return method.getReturnType();
            case SET:
            case UPDATE:
            case DELETE:
                return method.getParameterTypes()[0];
            default:
                throw new AutomatedDataException("Unsupported operation '" + operationType + "'");
        }
    }

}
