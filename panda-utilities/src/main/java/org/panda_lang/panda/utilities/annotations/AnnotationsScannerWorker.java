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
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.adapter.JavassistAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AnnotationsScannerWorker {

    private final Set<? extends AnnotationsScannerResource<?>> resources;
    private final List<Predicate<AnnotationsScannerFile>> fileFilters;
    private final List<Predicate<ClassFile>> pseudoClassFilters;
    private final List<Predicate<Class<?>>> classFilters;

    AnnotationsScannerWorker(AnnotationsScannerWorkerBuilder builder) {
        this.resources = builder.resources;
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
        JavassistAdapter javassistAdapter = new JavassistAdapter();

        for (AnnotationsScannerFile annotationsScannerFile : resource) {
            Class<?> clazz = scanFile(javassistAdapter, annotationsScannerFile);

            if (clazz == null) {
                continue;
            }

            classes.add(clazz);
        }

        return classes;
    }

    private @Nullable Class<?> scanFile(JavassistAdapter javassistAdapter, AnnotationsScannerFile file) {
        for (Predicate<AnnotationsScannerFile> fileFilter : fileFilters) {
            if (!fileFilter.test(file)) {
                return null;
            }
        }

        ClassFile pseudoClass = javassistAdapter.getOfCreateClassObject(file);

        for (Predicate<ClassFile> pseudoClassFilter : pseudoClassFilters) {
            if (!pseudoClassFilter.test(pseudoClass)) {
                return null;
            }
        }

        Class<?> clazz = AnnotationsScannerUtils.forName(file.getClassPath());

        for (Predicate<Class<?>> classFilter : classFilters) {
            if (!classFilter.test(clazz)) {
                return null;
            }
        }

        if (clazz == null) {
            return null;
        }

       return clazz;
    }

}
