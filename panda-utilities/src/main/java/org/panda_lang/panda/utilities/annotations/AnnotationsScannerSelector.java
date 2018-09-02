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

import org.panda_lang.panda.utilities.annotations.monads.AnnotationsSelector;
import org.panda_lang.panda.utilities.annotations.monads.selectors.MethodAnnotationSelector;
import org.panda_lang.panda.utilities.annotations.monads.selectors.SubTypeSelector;
import org.panda_lang.panda.utilities.annotations.monads.selectors.TypeAnnotationSelector;
import org.panda_lang.panda.utilities.commons.objects.TimeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AnnotationsScannerSelector {

    private final AnnotationsScannerProcess process;
    private final AnnotationScannerStore store;

    public AnnotationsScannerSelector(AnnotationsScannerProcess process, AnnotationScannerStore store) {
        this.process = process;
        this.store = store;
    }

    public <T> Collection<T> select(AnnotationsSelector<T> selector) {
        long uptime = System.nanoTime();

        try {
            Collection<T> selected = selector.select(process, store);
            process.getScanner().getLogger().debug("Selected classes: " + selected.size() + " in " + TimeUtils.toMilliseconds(System.nanoTime() - uptime));
            return selected;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<Class<? extends T>> selectSubtypesOf(Class<? extends T> type) {
        Set<Class<? extends T>> selectedClasses = new HashSet<>();

        for (Class<?> clazz : select(new SubTypeSelector(type))) {
            selectedClasses.add((Class<? extends T>) clazz);
        }

        return selectedClasses;
    }

    public Collection<Class<?>> selectTypesAnnotatedWith(Class<? extends Annotation> annotationType) {
        return select(new TypeAnnotationSelector(annotationType));
    }

    public Collection<Method> selectMethodsAnnotatedWith(Class<? extends Annotation> annotationType) {
        return select(new MethodAnnotationSelector(annotationType));
    }

}
