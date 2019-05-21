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

package org.panda_lang.panda.utilities.annotations.resource;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerResource;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

class SystemAnnotationScannerResource extends AnnotationsScannerResource<SystemAnnotationScannerFile> {

    private final File file;

    SystemAnnotationScannerResource(URL url) throws URISyntaxException {
        super(url);
        this.file = new File(url.toURI());

        if (!file.isDirectory() || !file.canRead()) {
            throw new RuntimeException("Cannot use dir " + file);
        }
    }

    @Override
    public Iterator<SystemAnnotationScannerFile> iterator() {
        return new Iterator<SystemAnnotationScannerFile>() {

            Stack<File> stack = new Stack<>();

            {
                stack.addAll(listFiles(file));
            }

            @Override
            public @Nullable SystemAnnotationScannerFile next() {
                File file = stack.pop();

                if (file.isDirectory()) {
                    stack.addAll(listFiles(file));

                    if (stack.isEmpty()) {
                        return null;
                    }

                    return next();
                }

                return new SystemAnnotationScannerFile(SystemAnnotationScannerResource.this, file);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }
        };
    }

    private List<File> listFiles(File file) {
        File[] files = file.listFiles();

        if (files == null) {
            return Collections.emptyList();
        }

        for (int i = 0, filesLength = files.length; i < filesLength; i++) {
            File element = files[i];

            if (element.isFile() && !element.toString().endsWith(".class")) {
                files[i] = null;
            }
        }

        return ArrayUtils.asList(files);
    }

    @Override
    public Iterable<@Nullable SystemAnnotationScannerFile> getFiles() {
        if (file == null || !file.exists()) {
            return Collections.emptyList();
        }

        return this;
    }

    @Override
    public String getPath() {
        if (file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }

        return StringUtils.replace(file.getPath(), "\\", "/");
    }

}