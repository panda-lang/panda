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

import org.panda_lang.panda.utilities.autodata.data.repository.DataHandler;
import org.panda_lang.panda.utilities.autodata.data.repository.DataStream;
import org.panda_lang.panda.utilities.autodata.data.entity.EntityScheme;

import java.util.Map;

final class InMemoryDataHandler<T> implements DataHandler<T> {

    private final InMemoryDataController<T> controller;
    private final EntityScheme entityScheme;

    InMemoryDataHandler(InMemoryDataController<T> controller, EntityScheme entityScheme) {
        this.controller = controller;
        this.entityScheme = entityScheme;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T create(Object[] values) throws Exception {
        Class<?>[] types = new Class<?>[values.length];

        for (int index = 0; index < values.length; index++) {
            types[index] = values[index].getClass(); // todo: null check
        }

        T value = (T) entityScheme.getRootClass().getConstructor(types).newInstance(values); // todo: xxx
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

}
