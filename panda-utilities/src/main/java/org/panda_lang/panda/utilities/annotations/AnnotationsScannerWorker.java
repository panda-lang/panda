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

import org.panda_lang.panda.utilities.annotations.resource.AnnotationsScannerFile;
import org.panda_lang.panda.utilities.annotations.resource.AnnotationsScannerResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AnnotationsScannerWorker {

    private final Set<? extends AnnotationsScannerResource<?>> resources;
    private final List<Predicate<String>> offlineFilters;
    private final List<Predicate<Class<?>>> onlineFilters;

    AnnotationsScannerWorker(Set<? extends AnnotationsScannerResource<?>> resources, List<Predicate<String>> offlineFilters, List<Predicate<Class<?>>> onlineFilters) {
        this.resources = resources;
        this.offlineFilters = offlineFilters;
        this.onlineFilters = onlineFilters;
    }

    public Set<Class<?>> scan() {
        Set<Class<?>> classes = new HashSet<>();

        for (AnnotationsScannerResource<?> resource : resources) {
            classes.addAll(scanResource(resource));
        }

        return classes;
    }

    private Set<Class<?>> scanResource(AnnotationsScannerResource<?> resource) {
        Set<Class<?>> classes = new HashSet<>();

        for (AnnotationsScannerFile annotationsScannerFile : resource) {

            for (Predicate<String> offlineFilter : offlineFilters) {
                if (!offlineFilter.test(annotationsScannerFile.getClassPath())) {
                    continue;
                }

                Class<?> clazz = AnnotationsScannerUtils.forName(annotationsScannerFile.getClassPath(), null);

                if (clazz == null) {
                    continue;
                }

                classes.add(clazz);
            }
        }

        return classes;
    }

}
