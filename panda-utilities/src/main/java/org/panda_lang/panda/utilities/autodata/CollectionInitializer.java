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

package org.panda_lang.panda.utilities.autodata;

import org.panda_lang.panda.utilities.autodata.data.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.DataFactory;
import org.panda_lang.panda.utilities.autodata.data.DataRepository;
import org.panda_lang.panda.utilities.inject.Injector;
import org.panda_lang.panda.utilities.inject.InjectorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

final class CollectionInitializer {

    private final Injector injector;
    private final DataFactory dataFactory = new DataFactory();

    CollectionInitializer(Injector injector) {
        this.injector = injector;
    }

    public Collection<DataCollection> initialize(Collection<CollectionStereotype> stereotypes) {
        initializeRepositories(stereotypes.stream()
                .map(stereotype -> stereotype.repositoryClass)
                .collect(Collectors.toList()));

        return stereotypes.stream()
                .map(stereotype -> dataFactory.createCollection(stereotype.name, tryNewInstance(stereotype.serviceClass)))
                .collect(Collectors.toList());
    }

    private void initializeRepositories(Collection<Class<? extends DataRepository>> classes) {
        classes.stream()
                .filter(Objects::nonNull)
                .map(clazz -> Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { clazz }, (proxy, method, args) -> null))
                .forEach(repository -> injector.getResources().on(repository.getClass()).assignInstance(repository));
    }

    private <T> T tryNewInstance(Class<T> type) {
        try {
            return injector.newInstance(type);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | InjectorException e) {
            throw new AutomatedDataException("Cannot create service instance: " + e.getMessage());
        }
    }

}
