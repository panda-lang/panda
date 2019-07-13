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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

final class RepositoryGenerator {

    private final Class<? extends DataRepository> repositoryClass;
    private final RepositoryMethodGenerator methodGenerator = new RepositoryMethodGenerator();

    RepositoryGenerator(Class<? extends DataRepository> repositoryClass) {
        this.repositoryClass = repositoryClass;
    }

    protected DataRepository<?> generate(DataController<?> controller, CollectionScheme collectionScheme) {
        Map<String, RepositoryGeneratorFunction> generatedFunctions = new HashMap<>();

        for (Method method : repositoryClass.getMethods()) {
            generatedFunctions.put(method.getName(), methodGenerator.generateMethod(controller, collectionScheme, method));
        }

        RepositoryInvocationHandler handler = new RepositoryInvocationHandler(generatedFunctions);
        return (DataRepository<?>) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { repositoryClass }, handler);
    }

}
