/*
 * Copyright (c) 2015-2018 Dzikoysk
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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.collection.ADSCollection;
import org.panda_lang.panda.utilities.autodata.database.ADSDatabase;

import java.util.Map;

public class AutomatedDataSpace {

    private final Map<String, ADSCollection> collections;
    private final Map<String, ADSDatabase> databases;

    public AutomatedDataSpace(AutomatedDataSpaceBuilder builder) {
        this.collections = builder.collections;
        this.databases = builder.databases;
    }

    public AutomatedDataInterface createInterface() {
        return new AutomatedDataInterface(this);
    }

    public @Nullable ADSDatabase getDatabase(String databaseName) {
        return this.databases.get(databaseName);
    }

    public @Nullable ADSCollection getCollection(String collectionName) {
        return this.collections.get(collectionName);
    }

    public static AutomatedDataSpaceBuilder builder() {
        return new AutomatedDataSpaceBuilder();
    }

}
