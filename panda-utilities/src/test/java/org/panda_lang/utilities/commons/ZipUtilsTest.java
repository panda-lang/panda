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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.panda_lang.utilities.commons.collection.Node;
import org.panda_lang.utilities.commons.collection.Sets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

class ZipUtilsTest {

    @TempDir
    public static Path directoryPath;

    @Test
    void extract() throws IOException {
        File outputDirectory = directoryPath.toFile();

        BufferedInputStream stream = new BufferedInputStream(ZipUtils.class.getResourceAsStream("/commons/zip-utils-test.zip"));
        ZipInputStream zipStream = new ZipInputStream(stream);
        ZipUtils.extract(zipStream, outputDirectory);

        File directory = new File(outputDirectory, "directory");
        Assertions.assertTrue(directory.exists());

        Node<File> map = FileUtils.collectFiles(directory);
        Assertions.assertEquals("directory", map.getElement().getName());

        Set<String> files = map.collectLeafs(File::isFile).stream()
                .map(File::getName)
                .collect(Collectors.toSet());
        Assertions.assertEquals(Sets.newHashSet("1.txt", "2.txt"), files);
    }

}