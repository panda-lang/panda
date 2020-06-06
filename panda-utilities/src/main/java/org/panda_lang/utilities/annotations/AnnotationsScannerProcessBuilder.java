/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.annotations;

import javassist.bytecode.ClassFile;
import org.panda_lang.utilities.annotations.monads.AnnotationsFilter;
import org.panda_lang.utilities.annotations.monads.filters.AnonymousFileFilter;
import org.panda_lang.utilities.annotations.monads.filters.JavaFilter;
import org.panda_lang.utilities.annotations.monads.filters.PackageFileFilter;
import org.panda_lang.utilities.commons.collection.Sets;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationsScannerProcessBuilder {

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

    /**
     * Add default filters:
     *
     * <ul>
     * <li>
     * URL filters:
     * {@link org.panda_lang.utilities.annotations.monads.filters.JavaFilter}
     * </li>
     * <li>
     * File filters:
     * {@link org.panda_lang.utilities.annotations.monads.filters.AnonymousFileFilter},
     * {@link org.panda_lang.utilities.annotations.monads.filters.PackageFileFilter}
     * </li>
     * <li>
     * Class filters:
     * {@link org.panda_lang.utilities.annotations.monads.filters.PublicClassFileFilter}
     * </li>
     * </ul>
     *
     * @return the builder instance
     */
    public AnnotationsScannerProcessBuilder addDefaultFilters() {
        addURLFilters(new JavaFilter());
        addFileFilters(new AnonymousFileFilter());
        return this;
    }

    /**
     * Add default filters for project-only packages
     *
     * @param packageNames the main package of project
     * @return the instance of builder
     */
    public AnnotationsScannerProcessBuilder addDefaultProjectFilters(String... packageNames) {
        return this.addFileFilters(new PackageFileFilter(false, packageNames));
    }

    @SafeVarargs
    public final AnnotationsScannerProcessBuilder addURLFilters(AnnotationsFilter<URL>... urlFilters) {
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

    /**
     * Fetch offline classes using these filters
     *
     * @return the process with fetched classes
     */
    public AnnotationsScannerProcess fetch() {
        return new AnnotationsScannerProcess(this).fetch();
    }

}
