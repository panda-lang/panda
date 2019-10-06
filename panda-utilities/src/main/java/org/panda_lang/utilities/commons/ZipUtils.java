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

package org.panda_lang.utilities.commons;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtils {

    private static final int  BUFFER_SIZE = 4096;

    public static void extract(ZipInputStream zip, File target) throws IOException {
        try {
            ZipEntry entry;
            String name, dir;

            while ((entry = zip.getNextEntry()) != null) {
                name = entry.getName();

                if (entry.isDirectory()) {
                    mkdirs(target, name);
                    continue;
                }

                /* this part is necessary because file entry can come before
                 * directory entry where is file located
                 * i.e.:
                 *   /foo/foo.txt
                 *   /foo/
                 */
                dir = part(name);

                if (dir != null) {
                    mkdirs(target, dir);
                }

                extractFile(zip, target, name);
            }
        } finally {
            zip.close();
        }
    }

    private static void extractFile(ZipInputStream zip, File target, String name) throws IOException{
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(target,name)));
        int count;

        while ((count = zip.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }

        out.close();
    }

    private static void mkdirs(File target, String path) {
        File d = new File(target, path);

        if (!d.exists()) {
            d.mkdirs();
        }
    }

    private static @Nullable String part(String name) {
        int s = name.lastIndexOf( File.separatorChar );
        return s == -1 ? null : name.substring( 0, s );
    }

}
