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

import org.panda_lang.panda.utilities.autodata.data.DataRepository;

public final class CollectionStereotype {

    private final SpaceCreator creator;

    protected String name;
    protected Class<?> type;
    protected Class<?> serviceClass;
    protected Class<? extends DataRepository> repositoryClass;

    public CollectionStereotype(SpaceCreator creator) {
        this.creator = creator;
    }

    public CollectionStereotype name(String databaseName) {
        this.name = databaseName;
        return this;
    }

    public CollectionStereotype type(Class<?> type) {
        this.type = type;
        return this;
    }

    public CollectionStereotype service(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
        return this;
    }

    public CollectionStereotype repository(Class<? extends DataRepository> repositoryClass) {
        this.repositoryClass = repositoryClass;
        return this;
    }

    public SpaceCreator append() {
        creator.stereotypes.add(this);
        return creator;
    }

}
