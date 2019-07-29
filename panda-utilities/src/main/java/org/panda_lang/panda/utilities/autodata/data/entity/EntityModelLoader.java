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

import org.panda_lang.panda.utilities.autodata.AutomatedDataException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class EntityModelLoader {

    private static final MethodModelLoader METHOD_LOADER = new MethodModelLoader();

    private final Map<Class<?>, EntityModel> cached = new HashMap<>();

    protected EntityModel load(Class<?> entityClass) {
        if (cached.containsKey(entityClass)) {
            return cached.get(entityClass);
        }

        if (!entityClass.isInterface()) {
            throw new AutomatedDataException("Entity class is not an interface (source: " + entityClass.toGenericString() + ")");
        }

        Map<String, Property> properties = new HashMap<>();
        Collection<MethodModel> methods = new ArrayList<>();

        for (Method method : entityClass.getDeclaredMethods()) {
            MethodModel schemeMethod = METHOD_LOADER.load(properties, method);
            methods.add(schemeMethod);
        }

        EntityModel scheme = new EntityModel(entityClass, properties, methods);
        cached.put(entityClass, scheme);

        return scheme;
    }

}
