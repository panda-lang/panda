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

import java.util.HashMap;
import java.util.Map;

public final class AutomatedDataSpace {

    private final Map<String, DataCollection> collections = new HashMap<>();

    AutomatedDataSpace() { }

    protected void addCollection(DataCollection collection) {
        collections.put(collection.getName(), collection);
    }

    public DataCollection getCollection(String collectionName) {
        return this.collections.get(collectionName);
    }

    public static SpaceCreator initialize() {
        return new SpaceCreator();
    }

}
