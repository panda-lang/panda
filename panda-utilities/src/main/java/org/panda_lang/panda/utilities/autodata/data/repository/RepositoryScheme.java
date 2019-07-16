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
import org.panda_lang.panda.utilities.autodata.data.entity.EntitySchemeMethod;

import java.util.Collection;
import java.util.Map;

public final class RepositoryScheme {

    private final DataRepository<?> repository;
    private final Map<RepositoryOperationType, Collection<EntitySchemeMethod>> methods;
    private final CollectionScheme collectionScheme;

    RepositoryScheme(DataRepository<?> repository, Map<RepositoryOperationType, Collection<EntitySchemeMethod>> methods, CollectionScheme collectionScheme) {
        this.repository = repository;
        this.methods = methods;
        this.collectionScheme = collectionScheme;
    }

    public CollectionScheme getCollectionScheme() {
        return collectionScheme;
    }

    public Map<RepositoryOperationType, Collection<EntitySchemeMethod>> getMethods() {
        return methods;
    }

    public DataRepository<?> getRepository() {
        return repository;
    }

}
