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

import org.panda_lang.panda.utilities.autodata.data.collection.DataCollectionStereotype;
import org.panda_lang.panda.utilities.autodata.data.repository.DataController;
import org.panda_lang.panda.utilities.inject.DependencyInjection;
import org.panda_lang.panda.utilities.inject.Injector;

import java.util.ArrayList;
import java.util.List;

public final class AutomatedDataSpaceCreator {

    protected final Injector injector;
    protected final Class<? extends DataController> controller;
    protected final List<DataCollectionStereotype> stereotypes = new ArrayList<>();

    AutomatedDataSpaceCreator(Class<? extends DataController> controller) {
        this.controller = controller;
        this.injector = DependencyInjection.createInjector();
    }

    public AutomatedDataSpaceCreator withStereotype(DataCollectionStereotype stereotype) {
        stereotypes.add(stereotype);
        return this;
    }

    public DataCollectionStereotype.DataCollectionStereotypeBuilder createCollection() {
        return DataCollectionStereotype.builder(this);
    }

    public AutomatedDataSpace collect() {
        if (controller == null) {
            throw new AutomatedDataException("Missing data controller");
        }

        try {
            AutomatedDataSpace automatedDataSpace = new AutomatedDataSpace(controller.newInstance());

            AutomatedDataSpaceInitializer dataSpaceInitializer = new AutomatedDataSpaceInitializer(automatedDataSpace, injector);
            dataSpaceInitializer.initialize(stereotypes).forEach(automatedDataSpace::addCollection);

            return automatedDataSpace;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AutomatedDataException("Cannot initialize data space: " + e.getMessage());
        }
    }

}
