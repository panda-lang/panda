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
import org.panda_lang.panda.utilities.inject.Injector;

final class RepositorySchemeLoader {

    private static final RepositoryProxyGenerator REPOSITORY_GENERATOR = new RepositoryProxyGenerator();

    public RepositoryScheme load(DataController<?> controller, Injector injector, CollectionScheme collectionScheme) {
        RepositoryScheme repositoryScheme = REPOSITORY_GENERATOR.generate(controller, collectionScheme);

        injector.getResources()
                .on(repositoryScheme.getRepository().getClass())
                .assignInstance(repositoryScheme.getRepository());

        return repositoryScheme;
    }

}
