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
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.AnonymousFileFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.JavaFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.PackageFileFilter;
import org.panda_lang.panda.utilities.annotations.monads.filters.PublicClassFileFilter;
import org.panda_lang.panda.utilities.commons.collection.Sets;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationsScannerProcessBuilder {

    protected final AnnotationsScanner scanner;
    protected final AnnotationScannerStore store;
    protected final List<AnnotationsFilter<URL>> urlFilters;
    protected final List<AnnotationsFilter<AnnotationsScannerFile>> fileFilters;
    protected final List<AnnotationsFilter<ClassFile>> classFileFilters;

    AnnotationsScannerProcessBuilder(AnnotationsScanner scanner, AnnotationScannerStore store) {
        this.scanner = scanner;
        this.store = store;
        this.urlFilters = new ArrayList<>(1);
        this.fileFilters = new ArrayList<>(1);
        this.classFileFilters = new ArrayList<>(1);
    }

    public AnnotationsScannerProcess fetch() {
        return new AnnotationsScannerProcess(this).fetch();
    }

    public AnnotationsScannerProcessBuilder addDefaultFilters() {
        addURLFilter(new JavaFilter());
        addFileFilters(new AnonymousFileFilter(), new PackageFileFilter(true, AnnotationsScannerConstants.PANDA_PACKAGES));
        addClassFileFilters(new PublicClassFileFilter());
        return this;
    }

    public AnnotationsScannerProcessBuilder addDefaultProjectFilters(String... packageNames) {
        return this
                .addURLFilter(new JavaFilter())
                .addFileFilters(new PackageFileFilter(false, packageNames));
    }

    public AnnotationsScannerProcessBuilder addURLFilter(AnnotationsFilter<URL>... urlFilters) {
        this.urlFilters.addAll(Sets.newHashSet(urlFilters));
        return this;
    }

    public AnnotationsScannerProcessBuilder addFileFilters(AnnotationsFilter<AnnotationsScannerFile>... fileFilters) {
        this.fileFilters.addAll(Sets.newHashSet(fileFilters));
        return this;
    }

    public AnnotationsScannerProcessBuilder addClassFileFilters(AnnotationsFilter<ClassFile>... classFileFilters) {
        this.classFileFilters.addAll(Sets.newHashSet(classFileFilters));
        return this;
    }

}
