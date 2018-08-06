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
import org.panda_lang.panda.utilities.annotations.AnnotationsScannerResource;

import java.io.File;
import java.net.URL;

enum AnnotationsScannerResourceType implements AnnotationsScannerResourceURLType {

    JAR_FILE {
        @Override
        public boolean matches(URL url) {
            return url.getProtocol().equals("file") && url.toExternalForm().contains(".jar");
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            return new JarAnnotationsScannerResource(url);
        }
    },

    DIRECTORY {
        @Override
        public boolean matches(URL url) throws Exception {
            return url.getProtocol().equals("file") && !url.toExternalForm().contains(".jar") && new File(url.toURI()).isDirectory();
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            return new SystemAnnotationScannerResource(url);
        }
    };

    /*,

    JAR_URL {
        @Override
        public boolean matches(URL url) {
            return "jar".equals(url.getProtocol()) || "zip".equals(url.getProtocol()) || "wsjar".equals(url.getProtocol());
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            try {
                URLConnection urlConnection = url.openConnection();

                if (urlConnection instanceof JarURLConnection) {
                    return new ZipDir(((JarURLConnection) urlConnection).getJarFile());
                }
            } catch (Throwable var3) {
                ;
            }

            java.io.File file = Vfs.getFile(url);
            return file != null ? new ZipDir(new JarFile(file)) : null;
        }
    },

    JAR_INPUT_STREAM {
        @Override
        public boolean matches(URL url) throws Exception {
            return url.toExternalForm().contains(".jar");
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            return new JarAnnotationsScannerResource(url);
        }
    },

    BUNDLE {
        @Override
        public boolean matches(URL url) throws Exception {
            return url.getProtocol().startsWith("bundle");
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            return Vfs.fromURL((URL)ClasspathHelper.contextClassLoader().loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", URL.class).invoke((Object)null, url));
        }
    },

    DIRECTORY {
        @Override
        public boolean matches(URL url) {
            return url.getProtocol().equals("file") && !url.toExternalForm().contains(".jar") && Vfs.getFile(url).isDirectory();
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) throws Exception {
            return new SystemDir(Vfs.getFile(url));
        }
    };
    */

    static @Nullable AnnotationsScannerResourceType detect(URL url) {
        for (AnnotationsScannerResourceType type : values()) {
            try {
                if (type.matches(url)) {
                    return type;
                }
            } catch (Exception e) {
                // e.printStackTrace(); mute
            }
        }

        return null;
    }

    static @Nullable AnnotationsScannerResource<?> createResource(AnnotationsScannerResourceType type, URL url) {
        try {
            return type.createResource(url);
        } catch (Exception e) {
            return null;
        }
    }

    static @Nullable AnnotationsScannerResource<?> createTypedResource(URL url) {
        AnnotationsScannerResourceType type = detect(url);
        return type != null ? createResource(type, url) : null;
    }

}
