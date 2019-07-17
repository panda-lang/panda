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

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionScheme;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityFactory;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityMethodScheme;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class RepositoryProxyGenerator {

    private static final RepositoryProxyMethodGenerator REPOSITORY_METHOD_GENERATOR =  new RepositoryProxyMethodGenerator();
    private static final EntityFactory ENTITY_FACTORY = new EntityFactory();

    protected RepositoryScheme generate(DataController<?> controller, CollectionScheme collectionScheme) {
        Class<? extends DataRepository> repositoryClass = collectionScheme.getRepositoryClass();

        Map<String, RepositoryProxyMethod> generatedFunctions = new HashMap<>();
        Map<RepositoryOperation, Collection<EntityMethodScheme>> methods = new HashMap<>();

        for (Method method : repositoryClass.getDeclaredMethods()) {
            RepositoryProxyMethod function = REPOSITORY_METHOD_GENERATOR.generateMethod(controller, collectionScheme, method);

            generatedFunctions.put(method.getName(), function);
            methods.computeIfAbsent(function.getOperationType(), (key) -> new ArrayList<>()).add(ENTITY_FACTORY.createEntitySchemeMethod(method));
        }

        RepositoryProxyInvocationHandler handler = new RepositoryProxyInvocationHandler(generatedFunctions);
        DataRepository<?> repository = (DataRepository<?>) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { repositoryClass }, handler);

        return new RepositoryScheme(repository, methods, collectionScheme);
    }

}
