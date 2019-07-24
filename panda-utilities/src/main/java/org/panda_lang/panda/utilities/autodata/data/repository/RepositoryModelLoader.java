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

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionModel;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.inject.Injector;

final class RepositoryModelLoader {

    private static final ProxyGenerator REPOSITORY_GENERATOR = new ProxyGenerator();

    protected RepositoryModel load(Injector injector, CollectionModel collectionModel) {
        RepositoryModel repositoryModel = REPOSITORY_GENERATOR.generate(collectionModel);

        injector.getResources()
                .on(repositoryModel.getRepository().getClass())
                .assignInstance(repositoryModel.getRepository());

        return repositoryModel;
    }

    protected void generateMethods(DataController controller, DataCollection collection, RepositoryModel repositoryModel) {
        REPOSITORY_GENERATOR.generateMethods(controller, collection, repositoryModel);
    }

}
