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

import org.jetbrains.annotations.NotNull;
import org.panda_lang.panda.utilities.autodata.collection.ADSCollection;

public class AutomatedDataInterface {

    private final AutomatedDataSpace automatedDataSpace;

    protected AutomatedDataInterface(AutomatedDataSpace automatedDataSpace) {
        this.automatedDataSpace = automatedDataSpace;
    }

    public void post(String collectionName, Object element) {
        ADSCollection collection = this.selectCollection(collectionName, element.getClass());
        collection.post(element);
    }

    public void loadAll() {

    }

    public void saveAll() {

    }

    @NotNull
    private ADSCollection<?> selectCollection(String collectionName, Class<?> type) {
        ADSCollection collection = automatedDataSpace.getCollection(collectionName);

        if (collection == null) {
            throw new AutomatedDataException("Collection of " + type + " does not exist");
        }

        if (type != null && collection.getType() != type) {
            throw new AutomatedDataException("Cannot get " + type + " from collection of " + collection.getType());
        }

        return collection;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String collectionName, Class<T> type, Object query) {
        ADSCollection collection = this.selectCollection(collectionName, type);
        return (T) collection.get(query);
    }

}
