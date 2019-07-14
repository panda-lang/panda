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
import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.repository.DataStream;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class InMemoryDataHandler<T> implements DataHandler<T> {

    private static final AtomicInteger ID = new AtomicInteger();

    private final int id;
    private final InMemoryDataController<T> controller;
    private DataCollection collection;

    InMemoryDataHandler(InMemoryDataController<T> controller) {
        this.id = ID.incrementAndGet();
        this.controller = controller;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T create(Object[] values) throws Exception {
        Class<?>[] types = new Class<?>[values.length];

        for (int index = 0; index < values.length; index++) {
            types[index] = values[index].getClass(); // todo: null check
        }

        T value = (T) collection.getEntityClass().getConstructor(DataHandler.class).newInstance(this);
        controller.getValues().add(value);

        return value;
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public void save(T o, Map<String, Object> changes) {

    }

    @Override
    public Object find(DataStream stream) {
        return null;
    }

    public void setCollection(DataCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getIdentifier() {
        return controller.getIdentifier() + id;
    }

}
