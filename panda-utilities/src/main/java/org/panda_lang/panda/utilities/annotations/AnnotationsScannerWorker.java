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

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.filters.Filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerWorker {

    private final Set<? extends AnnotationsScannerResource<?>> resources;
    private final MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter;
    private final List<Filter<AnnotationsScannerFile>> fileFilters;
    private final List<Filter<ClassFile>> pseudoClassFilters;
    private final List<Filter<Class<?>>> classFilters;

    AnnotationsScannerWorker(AnnotationsScannerWorkerBuilder builder) {
        this.resources = builder.resources;
        this.metadataAdapter = builder.metadataAdapter;
        this.fileFilters = builder.fileFilters;
        this.pseudoClassFilters = builder.pseudoClassFilters;
        this.classFilters = builder.classFilters;
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
            Class<?> clazz = scanFile(annotationsScannerFile);

            if (clazz == null) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

    private @Nullable Class<?> scanFile(AnnotationsScannerFile file) {
        for (Filter<AnnotationsScannerFile> fileFilter : fileFilters) {
            if (!fileFilter.check(metadataAdapter, file)) {
                return null;
            }
        }

        ClassFile pseudoClass;

        try {
            pseudoClass = metadataAdapter.getOfCreateClassObject(file);
        } catch (Exception e) {
            return null; // mute
        }

        for (Filter<ClassFile> pseudoClassFilter : pseudoClassFilters) {
            if (!pseudoClassFilter.check(metadataAdapter, pseudoClass)) {
                return null;
            }
        }

        Class<?> clazz = AnnotationsScannerUtils.forName(file.getClassPath());

        for (Filter<Class<?>> classFilter : classFilters) {
            if (!classFilter.check(metadataAdapter, clazz)) {
                return null;
            }
        }

        if (clazz == null) {
            return null;
        }

       return clazz;
    }

}
