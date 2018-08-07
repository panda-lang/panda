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
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.AnonymousFileFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.PackageFileFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.PublicClassFileFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.URLFilter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerProcessBuilder {

    protected final AnnotationsScanner scanner;
    protected final AnnotationScannerStore store;
    protected final Set<? extends AnnotationsScannerResource<?>> resources;
    protected final MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter;
    protected final List<AnnotationsFilter<URL>> urlFilters;
    protected final List<AnnotationsFilter<AnnotationsScannerFile>> fileFilters;
    protected final List<AnnotationsFilter<ClassFile>> classFileFilters;

    AnnotationsScannerProcessBuilder(AnnotationsScanner scanner, AnnotationScannerStore store) {
        this.scanner = scanner;
        this.store = store;
        this.resources = scanner.getResources();
        this.metadataAdapter = scanner.getMetadataAdapter();
        this.urlFilters = new ArrayList<>(1);
        this.fileFilters = new ArrayList<>(1);
        this.classFileFilters = new ArrayList<>(1);
    }

    public AnnotationsScannerProcessBuilder addDefaultFilters() {
        addURLFilter(new URLFilter(true, URLFilter.JRE_DIRECTORIES));
        addFileFilters(new AnonymousFileFilter(), new PackageFileFilter(true, PackageFileFilter.POPULAR_PACKAGES));
        addClassFileFilters(new PublicClassFileFilter());
        return this;
    }

    @SafeVarargs
    public final AnnotationsScannerProcessBuilder addURLFilter(AnnotationsFilter<URL>... urlFilters) {
        this.urlFilters.addAll(Sets.newHashSet(urlFilters));
        return this;
    }

    @SafeVarargs
    public final AnnotationsScannerProcessBuilder addFileFilters(AnnotationsFilter<AnnotationsScannerFile>... fileFilters) {
        this.fileFilters.addAll(Sets.newHashSet(fileFilters));
        return this;
    }

    @SafeVarargs
    public final AnnotationsScannerProcessBuilder addClassFileFilters(AnnotationsFilter<ClassFile>... classFileFilters) {
        this.classFileFilters.addAll(Sets.newHashSet(classFileFilters));
        return this;
    }

    public AnnotationsScannerProcess fetch() {
        return new AnnotationsScannerProcess(this).fetch();
    }

}
