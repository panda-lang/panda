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

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionScheme;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionFactory;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollectionStereotype;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryFactory;
import org.panda_lang.panda.utilities.inject.Injector;
import org.panda_lang.panda.utilities.inject.InjectorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

final class AutomatedDataSpaceInitializer {

    private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();
    private static final CollectionFactory COLLECTION_FACTORY = new CollectionFactory();

    private final AutomatedDataSpace automatedDataSpace;
    private final Injector injector;

    AutomatedDataSpaceInitializer(AutomatedDataSpace automatedDataSpace, Injector injector) {
        this.automatedDataSpace = automatedDataSpace;
        this.injector = injector;
    }

    protected Collection<? extends DataCollection> initialize(Collection<? extends DataCollectionStereotype> stereotypes) {
        initializeRepositories(stereotypes);

        Collection<? extends DataCollection> collections = createCollections(stereotypes);
        initializeController(stereotypes);

        return collections;
    }

    private void initializeController(Collection<? extends DataCollectionStereotype> stereotypes) {
        List<CollectionScheme> schemes = stereotypes.stream()
                .map(CollectionScheme::of)
                .collect(Collectors.toList());

        automatedDataSpace.getController().initialize(schemes);
    }

    private void initializeRepositories(Collection<? extends DataCollectionStereotype> stereotypes) {
        stereotypes.stream()
                .map(stereotype -> REPOSITORY_FACTORY.createRepository(automatedDataSpace.getController(), CollectionScheme.of(stereotype), stereotype.getRepositoryClass()))
                .forEach(repository -> injector.getResources().on(repository.getClass()).assignInstance(repository));
    }

    private Collection<? extends DataCollection> createCollections(Collection<? extends DataCollectionStereotype> stereotypes) {
        return stereotypes.stream()
                .map(stereotype -> {
                    try {
                        return COLLECTION_FACTORY.createCollection(stereotype, injector.newInstance((stereotype.getServiceClass())));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | InjectorException e) {
                        throw new AutomatedDataException("Cannot create service instance: " + e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }

}
