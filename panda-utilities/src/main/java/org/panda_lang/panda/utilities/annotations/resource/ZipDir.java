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

/**
 * an implementation of {@link org.reflections.vfs.Vfs.Dir} for {@link java.util.zip.ZipFile}
 */
/*
public class ZipDir implements org.reflections.vfs.Vfs.Dir {
    final java.util.zip.ZipFile jarFile;

    public ZipDir(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    public String getPath() {
        return jarFile.getInternalPath();
    }

    public Iterable<org.reflections.vfs.Vfs.File> getFiles() {
        return new Iterable<org.reflections.vfs.Vfs.File>() {
            public Iterator<org.reflections.vfs.Vfs.File> iterator() {
                return new AbstractIterator<org.reflections.vfs.Vfs.File>() {
                    final Enumeration<? extends ZipEntry> entries = jarFile.entries();

                    protected Vfs.File computeNext() {
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            if (!entry.isDirectory()) {
                                return new ZipFile(ZipDir.this, entry);
                            }
                        }

                        return endOfData();
                    }
                };
            }
        };
    }

    public void close() {
        try { jarFile.close(); } catch (IOException e) {
            if (Reflections.log != null) {
                Reflections.log.warn("Could not close JarFile", e);
            }
        }
    }

    @Override
    public String toString() {
        return jarFile.getInternalPath();
    }
}
*/
