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

import org.panda_lang.panda.utilities.autodata.AutomatedDataSpaceBuilder;

import java.util.HashMap;
import java.util.Map;

public class ADSCollectionBuilder {

    private final AutomatedDataSpaceBuilder builder;
    protected final Map<Class<?>, ADSCollectionHandler> handlers;
    protected Class<?> type;
    protected String name;
    protected ADSCollectionService service;

    public ADSCollectionBuilder(AutomatedDataSpaceBuilder builder) {
        this.builder = builder;
        this.handlers = new HashMap<>();
    }

    public ADSCollectionBuilder name(String collectionName) {
        this.name = collectionName;
        return this;
    }

    public ADSCollectionBuilder type(Class<?> collectionType) {
        this.type = collectionType;
        return this;
    }

    public ADSCollectionBuilder service(ADSCollectionService service) {
        this.service = service;
        return this;
    }

    public ADSCollectionBuilder handler(ADSCollectionHandler<?, ?, ?> handler) {
        this.handlers.put(handler.getQueryType(), handler);
        return this;
    }

    public AutomatedDataSpaceBuilder append() {
        return builder.addCollection(new ADSCollection(this));
    }

}