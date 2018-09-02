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

import javassist.bytecode.ClassFile;
import org.panda_lang.panda.utilities.annotations.AnnotationScannerStore;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerProcess;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsSelector;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TypeAnnotationSelector implements AnnotationsSelector<Class<?>> {

    private Class<? extends Annotation> annotationType;

    public TypeAnnotationSelector(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public Collection<Class<?>> select(AnnotationsScannerProcess process, AnnotationScannerStore store) throws Exception {
        Set<String> selectedClasses = new HashSet<>();

        for (ClassFile cachedClassFile : store.getCachedClassFiles()) {
            List<? extends String> annotationNames = process.getMetadataAdapter().getClassAnnotationNames(cachedClassFile);

            for (String annotationName : annotationNames) {
                if (!annotationType.getName().equals(annotationName)) {
                    continue;
                }

                selectedClasses.add(cachedClassFile.getName());
                break;
            }
        }

        return AnnotationsScannerUtils.forNames(process, selectedClasses);
    }

}
