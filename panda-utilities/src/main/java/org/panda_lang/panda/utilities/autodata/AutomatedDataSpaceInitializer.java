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

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionFactory;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionScheme;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollectionStereotype;
import org.panda_lang.panda.utilities.autodata.data.repository.DataRepository;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryFactory;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryModel;
import org.panda_lang.panda.utilities.autodata.orm.Berry;
import org.panda_lang.panda.utilities.inject.Injector;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

final class AutomatedDataSpaceInitializer {

    private static final CollectionFactory COLLECTION_FACTORY = new CollectionFactory();
    private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();

    private final AutomatedDataSpace automatedDataSpace;
    private final Injector injector;

    AutomatedDataSpaceInitializer(AutomatedDataSpace automatedDataSpace, Injector injector) {
        this.automatedDataSpace = automatedDataSpace;
        this.injector = injector;
    }

    /*

    Initialize:
        1 -> collection scheme:
           - name
          -> entity scheme:
             - name
             - class
             - properties & annotations
        2 -> initialize scheme
        3 -> repositories
        4 -> generate entity
        5 -> services & collections
        6 -> initialize controller collections

     */
    protected Collection<? extends DataCollection> initialize(Collection<? extends DataCollectionStereotype> stereotypes) {
        Collection<CollectionScheme> collectionSchemes = initializeSchemes(stereotypes);
        automatedDataSpace.getController().initializeSchemes(collectionSchemes);

        Collection<RepositoryModel> repositoryModels = initializeRepositories(collectionSchemes);
        injector.getResources().annotatedWith(Berry.class).assignHandler(initializeBerry(repositoryModels));

        Collection<? extends DataCollection> collections = createCollections(repositoryModels);
        automatedDataSpace.getController().initializeCollections(collections);

        return collections;
    }

    private Collection<CollectionScheme> initializeSchemes(Collection<? extends DataCollectionStereotype> stereotypes) {
        return stereotypes.stream()
                .map(CollectionScheme::of)
                .collect(Collectors.toList());
    }

    private Collection<RepositoryModel> initializeRepositories(Collection<? extends CollectionScheme> schemes) {
        return schemes.stream()
                .map(scheme -> REPOSITORY_FACTORY.createRepositoryScheme(injector, scheme))
                .collect(Collectors.toList());
    }

    private BiFunction<Class<?>, Berry, ?> initializeBerry(Collection<RepositoryModel> repositoryModels) {
        return (type, berry) -> {
            if (DataRepository.class.isAssignableFrom(type)) {
                Optional<? extends DataRepository<?>> dataRepository = repositoryModels.stream()
                        .filter(repositoryScheme -> repositoryScheme.getCollectionScheme().getName().equals(berry.value()))
                        .findFirst()
                        .map(RepositoryModel::getRepository);

                if (!dataRepository.isPresent()) {
                    throw new AutomatedDataException("Cannot resolve collection '" + berry.value() + "'");
                }

                return dataRepository.get();
            }

            return new AutomatedDataException("Unsupported by berry type: " + type);
        };
    }

    private Collection<? extends DataCollection> createCollections(Collection<RepositoryModel> schemes) {
        return schemes.stream()
                .map(scheme -> COLLECTION_FACTORY.createCollection(automatedDataSpace.getController(), injector, scheme))
                .collect(Collectors.toList());
    }

}
