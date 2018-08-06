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

package org.panda_lang.panda.utilities.annotations;

import org.panda_lang.panda.utilities.annotations.resource.AnnotationsScannerResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AnnotationsScannerWorkerBuilder {

    private final Set<? extends AnnotationsScannerResource<?>> resources;
    private final List<Predicate<String>> offlineFilters;
    private final List<Predicate<Class<?>>> onlineFilters;

    AnnotationsScannerWorkerBuilder(Set<? extends AnnotationsScannerResource<?>> resources) {
        this.resources = resources;
        this.offlineFilters = new ArrayList<>(1);
        this.onlineFilters = new ArrayList<>(1);
    }

    public AnnotationsScannerWorkerBuilder addOfflineFilter(Predicate<String> offlineFilter) {
        offlineFilters.add(offlineFilter);
        return this;
    }

    public AnnotationsScannerWorkerBuilder addOnlineFilter(Predicate<Class<?>> onlineFilter) {
        onlineFilters.add(onlineFilter);
        return this;
    }

    public AnnotationsScannerWorker build() {
        return new AnnotationsScannerWorker(resources, offlineFilters, onlineFilters);
    }

}
