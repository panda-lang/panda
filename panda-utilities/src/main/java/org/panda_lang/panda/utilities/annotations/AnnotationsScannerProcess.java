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
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsFilter;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;
import org.panda_lang.panda.utilities.commons.objects.TimeUtils;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnnotationsScannerProcess implements AnnotationsDisposable {

    private final AnnotationsScanner scanner;
    private final AnnotationScannerStore store;
    private final List<AnnotationsFilter<URL>> urlFilters;
    private final List<AnnotationsFilter<AnnotationsScannerFile>> fileFilters;
    private final List<AnnotationsFilter<ClassFile>> classFileFilters;

    AnnotationsScannerProcess(AnnotationsScannerProcessBuilder builder) {
        this.scanner = builder.scanner;
        this.store = builder.store;
        this.urlFilters = builder.urlFilters;
        this.fileFilters = builder.fileFilters;
        this.classFileFilters = builder.classFileFilters;
    }

    protected AnnotationsScannerProcess fetch() {
        long uptime = System.nanoTime();

        for (AnnotationsScannerResource<?> resource : scanner.getConfiguration().getResources()) {
            Set<ClassFile> classFiles = scanResource(resource);
            store.addClassFiles(classFiles);
        }

        scanner.getLogger().debug("Fetched class files: " + store.getAmountOfCachedClassFiles() + " in " + TimeUtils.toMilliseconds(System.nanoTime() - uptime));
        return this;
    }

    public AnnotationsScannerSelector createSelector() {
        return new AnnotationsScannerSelector(this, store);
    }

    private Set<ClassFile> scanResource(AnnotationsScannerResource<?> resource) {
        Set<ClassFile> classFiles = new HashSet<>();

        for (AnnotationsFilter<URL> urlFilter : urlFilters) {
            if (!urlFilter.check(getMetadataAdapter(), resource.getLocation())) {
                return classFiles;
            }
        }

        for (AnnotationsScannerFile annotationsScannerFile : resource) {
            if (annotationsScannerFile == null || !annotationsScannerFile.getOriginalPath().endsWith(".class")) {
                continue;
            }

            ClassFile classFile = scanFile(annotationsScannerFile);

            if (classFile == null) {
                continue;
            }

            classFiles.add(classFile);
        }

        return classFiles;
    }

    private @Nullable ClassFile scanFile(AnnotationsScannerFile file) {
        MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter = getMetadataAdapter();

        for (AnnotationsFilter<AnnotationsScannerFile> fileFilter : fileFilters) {
            if (!fileFilter.check(metadataAdapter, file)) {
                return null;
            }
        }

        ClassFile pseudoClass;

        try {
            pseudoClass = metadataAdapter.getOfCreateClassObject(scanner, file);
        } catch (Exception e) {
            return null; // mute
        }

        if (!StringUtils.isEmpty(pseudoClass.getSuperclass())) {
            store.addInheritors(pseudoClass.getSuperclass(), pseudoClass.getName());
        }

        if (pseudoClass.getInterfaces() != null) {
            for (String anInterface : pseudoClass.getInterfaces()) {
                store.addInheritors(anInterface, pseudoClass.getName());
            }
        }

        for (AnnotationsFilter<ClassFile> pseudoClassFilter : classFileFilters) {
            if (!pseudoClassFilter.check(metadataAdapter, pseudoClass)) {
                return null;
            }
        }

        return pseudoClass;
    }

    @Override
    public void dispose() {
        store.dispose();
    }

    public MetadataAdapter<ClassFile, FieldInfo, MethodInfo> getMetadataAdapter() {
        return scanner.getConfiguration().getMetadataAdapter();
    }

    protected AnnotationsScanner getScanner() {
        return scanner;
    }

}
