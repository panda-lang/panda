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

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionFactory;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionScheme;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollectionStereotype;
import org.panda_lang.panda.utilities.autodata.data.entity.DataEntity;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityFactory;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.repository.DataRepository;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryFactory;
import org.panda_lang.panda.utilities.autodata.data.repository.RepositoryScheme;
import org.panda_lang.panda.utilities.inject.Injector;
import org.panda_lang.panda.utilities.inject.InjectorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.stream.Collectors;

final class AutomatedDataSpaceInitializer {

    private static final CollectionFactory COLLECTION_FACTORY = new CollectionFactory();
    private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();
    private static final EntityFactory ENTITY_FACTORY = new EntityFactory();

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

        Collection<RepositoryScheme> repositorySchemes = initializeRepositories(collectionSchemes);
        Collection<? extends DataCollection> collections = createCollections(repositorySchemes);
        automatedDataSpace.getController().initializeCollections(collections);

        return collections;
    }

    private Collection<CollectionScheme> initializeSchemes(Collection<? extends DataCollectionStereotype> stereotypes) {
        return stereotypes.stream()
                .map(CollectionScheme::of)
                .collect(Collectors.toList());
    }

    private Collection<RepositoryScheme> initializeRepositories(Collection<? extends CollectionScheme> schemes) {
        return schemes.stream()
                .map(scheme -> {
                    DataRepository<?> repository = REPOSITORY_FACTORY.createRepository(automatedDataSpace.getController(), scheme);

                    injector.getResources()
                            .on(repository.getClass())
                            .assignInstance(repository);

                    return new RepositoryScheme(repository, scheme);
                })
                .collect(Collectors.toList());
    }

    private Collection<? extends DataCollection> createCollections(Collection<RepositoryScheme> schemes) {
        return schemes.stream()
                .map(scheme -> {
                    try {
                        CollectionScheme collectionScheme = scheme.getCollectionScheme();
                        DataHandler<?> dataHandler = automatedDataSpace.getController().getHandler(collectionScheme.getName());

                        Class<? extends DataEntity> entityClass = ENTITY_FACTORY.generateEntityClass(collectionScheme.getEntityScheme(), dataHandler);
                        Object service = injector.newInstance(scheme.getCollectionScheme().getServiceClass());

                        return COLLECTION_FACTORY.createCollection(collectionScheme, entityClass, service);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | InjectorException e) {
                        throw new AutomatedDataException("Cannot create service instance", e);
                    } catch (CannotCompileException | NotFoundException e) {
                        throw new AutomatedDataException("Cannot generate entity class", e);
                    }
                })
                .collect(Collectors.toList());
    }

}
