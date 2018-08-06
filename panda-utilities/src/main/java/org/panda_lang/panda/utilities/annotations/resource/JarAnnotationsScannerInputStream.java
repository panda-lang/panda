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

import java.io.IOException;
import java.io.InputStream;

class JarAnnotationsScannerInputStream extends InputStream {

    private final JarAnnotationsScannerResource resource;
    private final long fromIndex;
    private final long endIndex;

    JarAnnotationsScannerInputStream(JarAnnotationsScannerResource resource, long fromIndex, long endIndex) {
        this.resource = resource;
        this.fromIndex = fromIndex;
        this.endIndex = endIndex;
    }

    @Override
    public int read() throws IOException {
        if (resource.cursor >= fromIndex && resource.cursor <= endIndex) {
            int read = resource.jarInputStream.read();
            resource.cursor++;
            return read;
        }
        else {
            return -1;
        }
    }

}
