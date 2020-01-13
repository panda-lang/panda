/*
 * Copyright (c) 2015-2020 Dzikoysk
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
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.panda_lang.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.utilities.annotations.resource.SystemAnnotationScannerResourceIterator;
import org.panda_lang.utilities.commons.TimeUtils;

import java.util.Set;

public final class AnnotationsScannerProcess {

    private final AnnotationsScanner scanner;
    private final AnnotationScannerStore store;
    private final AnnotationsScannerProcessBuilder builder;

    AnnotationsScannerProcess(AnnotationsScannerProcessBuilder builder) {
        this.scanner = builder.scanner;
        this.store = builder.store;
        this.builder = builder;
    }

    protected AnnotationsScannerProcess fetch() {
        return fetch(scanner.getConfiguration().resources);
    }

    protected AnnotationsScannerProcess fetch(Set<AnnotationsScannerResource<?>> resources) {
        long uptime = System.nanoTime();

        AnnotationsScannerProcessWorker worker = new AnnotationsScannerProcessWorker(this);
        worker.fetch(resources);

        long time = System.nanoTime() - uptime;

        scanner.getLogger().debug("Fetched class files: " + store.getAmountOfCachedClassFiles() + " in " + TimeUtils.toMilliseconds(time));
        scanner.getLogger().debug("workerTime: " + TimeUtils.toMilliseconds(time - worker.fileTime));
        scanner.getLogger().debug("fileTime: " + TimeUtils.toMilliseconds(worker.fileTime - worker.jaTime));
        scanner.getLogger().debug("jaTime: " + TimeUtils.toMilliseconds(worker.jaTime));
        scanner.getLogger().debug("listTime: " + TimeUtils.toMilliseconds(SystemAnnotationScannerResourceIterator.listTime));
        scanner.getLogger().debug("");

        return this;
    }

    public AnnotationsScannerSelector createSelector() {
        return new AnnotationsScannerSelector(this, store);
    }

    public MetadataAdapter<ClassFile, FieldInfo, MethodInfo> getMetadataAdapter() {
        return scanner.getConfiguration().metadataAdapter;
    }

    public AnnotationScannerStore getStore() {
        return store;
    }

    public AnnotationsScannerProcessBuilder getProcessConfiguration() {
        return builder;
    }

    public AnnotationsScanner getAnnotationsScanner() {
        return scanner;
    }

}
