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

package org.panda_lang.utilities.annotations.resource;

import org.panda_lang.utilities.annotations.AnnotationsScannerFile;
import org.panda_lang.utilities.commons.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class SystemAnnotationScannerFile implements AnnotationsScannerFile {

    protected final SystemAnnotationScannerResource root;
    private final File file;
    private final String internal;

    SystemAnnotationScannerFile(SystemAnnotationScannerResource root, File file) {
        this.root = root;
        this.file = file;
        this.internal = StringUtils.replace(getOriginalPath(), "\\", "/").substring(root.getPath().length() + 1);
    }

    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getInternalPath() {
        return internal;
    }

    @Override
    public String getOriginalPath() {
        return file.getPath();
    }

}
