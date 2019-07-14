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

package org.panda_lang.panda.utilities.autodata.data.repository;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.AutomatedDataException;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionScheme;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class RepositoryMethodGenerator {

    private static final String CAMEL_CASE_PATTERN = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    protected @Nullable RepositoryGeneratorFunction generateMethod(DataController<?> controller, CollectionScheme collectionScheme, Method method) {
        List<String> elements = Arrays.stream(method.getName().split(CAMEL_CASE_PATTERN))
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        // System.out.println(elements.toString());

        String operation = elements.get(0);
        List<String> specification = elements.subList(1, elements.size());

        switch (operation) {
            case "create":
                return parameters -> controller.getHandler(collectionScheme.getName()).create(parameters);
            case "delete":
                return null;
            case "update":
                return null;
            case "find":
                return null;
            default:
                throw new AutomatedDataException("Unknown operation: '" + operation + "' (source: " + method.toGenericString() + ")");
        }
    }

}
