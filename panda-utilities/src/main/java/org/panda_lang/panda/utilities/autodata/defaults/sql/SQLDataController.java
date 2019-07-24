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

package org.panda_lang.panda.utilities.autodata.defaults.sql;

import org.panda_lang.panda.utilities.autodata.data.collection.CollectionModel;
import org.panda_lang.panda.utilities.autodata.data.collection.DataCollection;
import org.panda_lang.panda.utilities.autodata.data.repository.DataController;
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;

import java.util.Collection;

public final class SQLDataController<T> implements DataController {

    @Override
    public void initializeSchemes(Collection<? extends CollectionModel> schemes) {

    }

    @Override
    public void initializeCollections(Collection<? extends DataCollection> dataCollections) {

    }

    @Override
    public <ENTITY> DataHandler<ENTITY> getHandler(String collection) {
        return new SQLDataHandler<>();
    }

    @Override
    public String getIdentifier() {
        return null;
    }

}
