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

package org.panda_lang.utilities.annotations.resource;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.annotations.AnnotationsScannerResource;
import org.panda_lang.utilities.commons.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

public final class SystemAnnotationScannerResource extends AnnotationsScannerResource<SystemAnnotationScannerFile> {

    protected final File file;
    private final String path;

    SystemAnnotationScannerResource(URL url) throws URISyntaxException {
        super(url);
        this.file = new File(url.toURI());

        if (!file.isDirectory() || !file.canRead()) {
            throw new RuntimeException("Cannot use dir " + file);
        }

        this.path = StringUtils.replace(file.getPath(), "\\", "/");
    }

    @Override
    public Iterator<SystemAnnotationScannerFile> iterator() {
        return new SystemAnnotationScannerResourceIterator(this);
    }

    @Override
    public Iterable<@Nullable SystemAnnotationScannerFile> getFiles() {
        if (file == null) {
            return Collections.emptyList();
        }

        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

}