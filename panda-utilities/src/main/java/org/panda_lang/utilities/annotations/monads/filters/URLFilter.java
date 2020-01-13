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

package org.panda_lang.utilities.annotations.monads.filters;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import org.panda_lang.utilities.annotations.adapter.MetadataAdapter;
import org.panda_lang.utilities.annotations.monads.AnnotationsFilter;
import org.panda_lang.utilities.commons.StringUtils;

import java.net.URL;
import java.util.Arrays;

public class URLFilter implements AnnotationsFilter<URL> {

    private final boolean exclude;
    private final String[] paths;

    public URLFilter(String... paths) {
        this(false, paths);
    }

    public URLFilter(boolean exclude, String... paths) {
        this.exclude = exclude;

        this.paths = Arrays.stream(paths)
                .map(path -> StringUtils.replace(path, ".", "/"))
                .toArray(String[]::new);
    }

    @Override
    public boolean check(MetadataAdapter<ClassFile, FieldInfo, MethodInfo> metadataAdapter, URL element) {
        String url = element.toString();

        for (String path : paths) {
            if (url.contains(path)) {
                return !exclude;
            }
        }

        return exclude;
    }

}
