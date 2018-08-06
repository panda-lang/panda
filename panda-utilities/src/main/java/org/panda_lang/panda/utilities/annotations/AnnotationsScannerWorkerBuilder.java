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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class AnnotationsScannerWorkerBuilder {

    protected final Set<? extends AnnotationsScannerResource<?>> resources;
    protected final List<Predicate<AnnotationsScannerFile>> fileFilters;
    protected final List<Predicate<ClassFile>> pseudoClassFilters;
    protected final List<Predicate<Class<?>>> classFilters;

    AnnotationsScannerWorkerBuilder(Set<? extends AnnotationsScannerResource<?>> resources) {
        this.resources = resources;
        this.fileFilters = new ArrayList<>(1);
        this.pseudoClassFilters = new ArrayList<>(1);
        this.classFilters = new ArrayList<>(1);
    }

    public AnnotationsScannerWorkerBuilder addFileFilter(Predicate<AnnotationsScannerFile> fileFilter) {
        fileFilters.add(fileFilter);
        return this;
    }

    public AnnotationsScannerWorkerBuilder addPseudoClassFilter(Predicate<ClassFile> pseudoClassFilter) {
        pseudoClassFilters.add(pseudoClassFilter);
        return this;
    }

    public AnnotationsScannerWorkerBuilder addClassFilter(Predicate<Class<?>> classFilter) {
        classFilters.add(classFilter);
        return this;
    }

    public AnnotationsScannerWorker build() {
        return new AnnotationsScannerWorker(this);
    }

}
