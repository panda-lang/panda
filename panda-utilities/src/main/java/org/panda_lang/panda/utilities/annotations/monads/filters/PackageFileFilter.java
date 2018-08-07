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

package org.panda_lang.panda.utilities.annotations.monads.filters;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerFile;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsFilter;

public class PackageFileFilter implements AnnotationsFilter<AnnotationsScannerFile> {

    private final boolean exclude;
    private final String[] packages;

    public PackageFileFilter(boolean exclude, String... packages) {
        this.exclude = exclude;
        this.packages = packages;
    }

    @Override
    public boolean check(MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter, AnnotationsScannerFile element) {
        for (String packageName : packages) {
            if (AnnotationsScannerUtils.toClassPath(element.getOriginalPath()).startsWith(packageName)) {
                return !exclude;
            }

            if (AnnotationsScannerUtils.toClassPath(element.getInternalPath()).startsWith(packageName)) {
                return !exclude;
            }
        }

        return exclude;
    }

}
