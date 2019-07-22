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

package org.panda_lang.panda.utilities.autodata.defaults.virtual;

import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.repository.DataController;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.collection.CollectionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class InMemoryDataController<T> implements DataController<T> {

    private final Collection<T> values = new ArrayList<>();
    private final Map<String, InMemoryDataHandler<T>> handlers = new HashMap<>();

    @Override
    public void initializeSchemes(Collection<? extends CollectionModel> schemes) {
        schemes.forEach(scheme -> handlers.put(scheme.getName(), new InMemoryDataHandler<>(this)));
    }

    @Override
    public void initializeCollections(Collection<? extends DataCollection> dataCollections) {
        dataCollections.forEach(dataCollection -> handlers.get(dataCollection.getName()).setCollection(dataCollection));
    }

    @Override
    public DataHandler<T> getHandler(String collection) {
        return handlers.get(collection);
    }

    protected Collection<T> getValues() {
        return values;
    }

    @Override
    public String getIdentifier() {
        return "InMemory";
    }

}
