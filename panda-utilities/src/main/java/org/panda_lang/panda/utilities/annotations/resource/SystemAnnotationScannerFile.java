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

package org.panda_lang.panda.utilities.annotations.resource;

import org.panda_lang.panda.utilities.annotations.AnnotationsScannerFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class SystemAnnotationScannerFile implements AnnotationsScannerFile {

    private final SystemAnnotationScannerResource root;
    private final File file;

    SystemAnnotationScannerFile(SystemAnnotationScannerResource root, File file) {
        this.root = root;
        this.file = file;
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
    public String getOriginalPath() {
        return file.getPath();
    }

    @Override
    public String getInternalPath() {
        return getOriginalPath().replace("\\", "/").substring(root.getPath().length() + 1);
    }

}
