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

package org.panda_lang.panda.utilities.autodata.collection;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.autodata.AutomatedDataRest;

import java.util.Map;
import java.util.Map.Entry;

public class ADSCollection<T> implements AutomatedDataRest<T> {

    private final Class<T> type;
    private final String name;
    private final ADSCollectionService service;
    private final Map<Class<?>, ADSCollectionHandler> handlers;

    @SuppressWarnings("unchecked")
    protected ADSCollection(ADSCollectionBuilder builder) {
        this.type = (Class<T>) builder.type;
        this.name = builder.name;
        this.service = builder.service;
        this.handlers = builder.handlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void post(Object element) {
        for (Entry<Class<?>, ADSCollectionHandler> entry : handlers.entrySet()) {
            ADSCollectionHandler handler = entry.getValue();

            if (handler.getDataType() != element.getClass()) {
                continue;
            }

            handler.save(service, element);
        }
    }

    @Override
    public void patch(Object element) {

    }

    @Override
    public T put(Object element) {
        return null;
    }

    @Override
    public T delete(Object query) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable T get(Object query) {
        for (Entry<Class<?>, ADSCollectionHandler> entry : handlers.entrySet()) {
            if (entry.getKey() != query.getClass()) {
                continue;
            }

            ADSCollectionHandler handler = entry.getValue();
            Object value = handler.get(service, query);

            return (T) value;
        }

        return null;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
