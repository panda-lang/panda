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
import org.panda_lang.panda.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.panda.utilities.annotations.monads.AnnotationsFilter;

import java.net.URL;

public class JavaFilter implements AnnotationsFilter<URL> {

    @Override
    public boolean check(MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter, URL element) {
        return !isJavaPath(element.toExternalForm().replace("/", "."));
    }

    private boolean isJavaPath(String path) {
        return (path.contains("jre") || path.contains("jdk")) && (path.contains(".lib.") || path.contains(".bin."));
    }

}
