/*
 * Copyright (c) 2015-2019 Dzikoysk
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
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class AnnotationsScannerProcessWorker {

    private final AnnotationsScanner scanner;
    private final AnnotationsScannerProcess process;
    private final AnnotationScannerStore store;

    protected long fileTime;
    protected long jaTime;

    AnnotationsScannerProcessWorker(AnnotationsScannerProcess process) {
        this.process = process;
        this.scanner = process.getAnnotationsScanner();
        this.store = process.getStore();
    }

    protected AnnotationsScannerProcessWorker fetch(Collection<? extends AnnotationsScannerResource<?>> resources) {
        for (AnnotationsScannerResource<?> resource : resources) {
            Collection<ClassFile> classFiles = scanResource(resource);
            process.getStore().addClassFiles(classFiles);
        }

        return this;
    }

    private Collection<@Nullable ClassFile> scanResource(AnnotationsScannerResource<?> resource) {
        for (AnnotationsFilter<URL> urlFilter : process.getProcessConfiguration().urlFilters) {
            if (!urlFilter.check(process.getMetadataAdapter(), resource.getLocation())) {
                return Collections.emptyList();
            }
        }

        Collection<ClassFile> classFiles = new ArrayList<>();

        for (@Nullable AnnotationsScannerFile annotationsScannerFile : resource) {
            if (annotationsScannerFile == null) {
                continue;
            }

            long time = System.nanoTime();
            ClassFile classFile = scanFile(annotationsScannerFile);
            fileTime += (System.nanoTime() - time);

            if (classFile == null) {
                continue;
            }

            classFiles.add(classFile);
        }

        return classFiles;
    }

    private @Nullable ClassFile scanFile(AnnotationsScannerFile file) {
        MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter = process.getMetadataAdapter();

        for (AnnotationsFilter<AnnotationsScannerFile> fileFilter : process.getProcessConfiguration().fileFilters) {
            if (!fileFilter.check(metadataAdapter, file)) {
                return null;
            }
        }

        long time = System.nanoTime();
        ClassFile pseudoClass;

        try {
            pseudoClass = metadataAdapter.getOfCreateClassObject(scanner, file);
        } catch (Exception e) {
            return null; // mute
        }

        jaTime += (System.nanoTime() - time);

        if (!StringUtils.isEmpty(pseudoClass.getSuperclass())) {
            store.addInheritors(pseudoClass.getSuperclass(), pseudoClass.getName());
        }

        if (pseudoClass.getInterfaces() != null) {
            for (String anInterface : pseudoClass.getInterfaces()) {
                store.addInheritors(anInterface, pseudoClass.getName());
            }
        }

        for (AnnotationsFilter<ClassFile> pseudoClassFilter : process.getProcessConfiguration().classFileFilters) {
            if (!pseudoClassFilter.check(metadataAdapter, pseudoClass)) {
                return null;
            }
        }

        return pseudoClass;
    }

}
