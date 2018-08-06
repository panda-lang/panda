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

import com.google.common.collect.Sets;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.filters.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerWorkerBuilder {

    protected final AnnotationScannerStore store;
    protected final Set<? extends AnnotationsScannerResource<?>> resources;
    protected final MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter;
    protected final List<Filter<AnnotationsScannerFile>> fileFilters;
    protected final List<Filter<ClassFile>> pseudoClassFilters;
    protected final List<Filter<Class<?>>> classFilters;

    AnnotationsScannerWorkerBuilder(AnnotationsScanner scanner, AnnotationScannerStore store) {
        this.store = store;
        this.resources = scanner.getResources();
        this.metadataAdapter = scanner.getMetadataAdapter();

        this.fileFilters = new ArrayList<>(1);
        this.pseudoClassFilters = new ArrayList<>(1);
        this.classFilters = new ArrayList<>(1);
    }

    @SafeVarargs
    public final AnnotationsScannerWorkerBuilder addFileFilters(Filter<AnnotationsScannerFile>... fileFilters) {
        this.fileFilters.addAll(Sets.newHashSet(fileFilters));
        return this;
    }

    @SafeVarargs
    public final AnnotationsScannerWorkerBuilder addPseudoClassFilters(Filter<ClassFile>... pseudoClassFilters) {
        this.pseudoClassFilters.addAll(Sets.newHashSet(pseudoClassFilters));
        return this;
    }

    @SafeVarargs
    public final AnnotationsScannerWorkerBuilder addClassFilters(Filter<Class<?>>... classFilters) {
        this.classFilters.addAll(Sets.newHashSet(classFilters));
        return this;
    }

    public AnnotationsScannerProcess fetch() {
        return new AnnotationsScannerProcess(this).fetch();
    }

}
