/*
 * Copyright (c) 2020 Dzikoysk
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

import java.io.File;
import java.net.URL;

enum AnnotationsScannerResourceType implements AnnotationsScannerResourceURLType {

    JAR_FILE {
        @Override
        public boolean matches(URL url) {
            return url.getProtocol().equals("file") && url.toExternalForm().contains(".jar");
        }

        @Override
        public AnnotationsScannerResource createResource(URL url) {
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
