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

import org.panda_lang.panda.utilities.inject.DependencyInjection;
import org.panda_lang.panda.utilities.inject.Injector;

import java.util.ArrayList;
import java.util.List;

public final class SpaceCreator {

    protected final Injector injector;
    protected final List<CollectionStereotype> stereotypes = new ArrayList<>();

    SpaceCreator() {
        this.injector = DependencyInjection.createInjector(new SpaceInjectionController(this));
    }

    public CollectionStereotype createCollection() {
        return new CollectionStereotype(this);
    }

    public AutomatedDataSpace collect() {
        AutomatedDataSpace automatedDataSpace = new AutomatedDataSpace();

        CollectionInitializer collectionInitializer = new CollectionInitializer(injector);
        collectionInitializer.initialize(stereotypes).forEach(automatedDataSpace::addCollection);

        return automatedDataSpace;
    }

}
