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
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;

import java.util.Set;

public class AnnotationsScanner {

    private final Set<? extends AnnotationsScannerResource<?>> resources;
    private final MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter;

    protected AnnotationsScanner(Set<? extends AnnotationsScannerResource<?>> resources, MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter) {
        this.resources = resources;
        this.metadataAdapter = metadataAdapter;
    }

    public AnnotationsScannerWorkerBuilder createWorker() {
        return new AnnotationsScannerWorkerBuilder(this, new AnnotationScannerStore());
    }

    protected MetadataAdapter<ClassFile, FieldInfo, MethodInfo> getMetadataAdapter() {
        return metadataAdapter;
    }

    protected Set<? extends AnnotationsScannerResource<?>> getResources() {
        return resources;
    }

    public static AnnotationsScannerBuilder builder() {
        return new AnnotationsScannerBuilder();
    }

}
