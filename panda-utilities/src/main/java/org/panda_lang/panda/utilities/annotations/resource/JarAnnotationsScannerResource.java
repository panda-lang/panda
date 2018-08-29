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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerFile;
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerResource;
import org.panda_lang.panda.utilities.commons.io.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

class JarAnnotationsScannerResource extends AnnotationsScannerResource<AnnotationsScannerFile> implements Closeable {

    protected JarInputStream jarInputStream;
    protected long cursor = 0;
    protected long nextCursor = 0;

    JarAnnotationsScannerResource(URL url) {
        super(url);
    }

    @Override
    public Iterator<AnnotationsScannerFile> iterator() {
        try {
            jarInputStream = new JarInputStream(getLocation().openConnection().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Could not open url connection", e);
        }

        return new Iterator<AnnotationsScannerFile>() {

            private ZipEntry entry;

            @Override
            public @Nullable AnnotationsScannerFile next() {
                while (entry != null) {
                    long size = entry.getSize();

                    if (size < 0) {
                        size = 0xffffffffL + size; //JDK-6916399
                    }

                    nextCursor += size;

                    if (!entry.isDirectory()) {
                        return new JarAnnotationsScannerFile(JarAnnotationsScannerResource.this, entry, cursor, nextCursor);
                    }

                    if (!hasNext()) {
                        break;
                    }
                }

                return null;
            }

            @Override
            public boolean hasNext() {
                try {
                    entry = jarInputStream.getNextJarEntry();

                    if (entry != null && !entry.getName().endsWith(".class")) {
                        return hasNext();
                    }

                    return entry != null;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }

    @Override
    public void close() {
        IOUtils.close(jarInputStream);
    }

    @Override
    public Iterable<AnnotationsScannerFile> getFiles() {
        return this;
    }

}
