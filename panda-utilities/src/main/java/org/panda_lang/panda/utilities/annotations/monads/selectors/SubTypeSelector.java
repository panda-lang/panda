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

package org.panda_lang.panda.utilities.annotations.monads.selectors;

import org.panda_lang.panda.utilities.annotations.AnnotationScannerStore;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsSelector;

import java.util.Collection;

public class SubTypeSelector implements AnnotationsSelector<Class<?>> {

    private final Class<?> type;

    public SubTypeSelector(Class<?> type) {
        this.type = type;
    }

    @Override
    public Collection<Class<?>> select(AnnotationsScannerProcess process, AnnotationScannerStore store) {
        return AnnotationsScannerUtils.forNames(process, store.getInheritorsOf(type.getName()));
    }

}
