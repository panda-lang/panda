/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.utilities

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import panda.utilities.collection.Sets

import java.nio.file.Path
import java.util.stream.Collectors
import java.util.zip.ZipInputStream

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
class ZipUtilsTest {

    @TempDir
    public static Path directoryPath;

    @Test
    void extract() throws IOException {
        def outputDirectory = directoryPath.toFile()

        def stream = new BufferedInputStream(ZipUtils.class.getResourceAsStream("/commons/zip-utils-test.zip"))
        def zipStream = new ZipInputStream(stream)
        ZipUtils.extract(zipStream, outputDirectory)

        def directory = new File(outputDirectory, "directory")
        assertTrue directory.exists()

        def map = FileUtils.collectFiles(directory)
        assertEquals "directory", map.getElement().getName()

        def files = map.collectLeafs(file -> file.isFile()).stream()
                .map(file -> file.getName())
                .collect(Collectors.toSet())
        assertEquals Sets.newHashSet("1.txt", "2.txt"), files
    }

}