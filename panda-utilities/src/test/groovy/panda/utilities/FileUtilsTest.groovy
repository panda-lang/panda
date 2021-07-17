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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

import java.nio.file.Path

import static org.junit.jupiter.api.Assertions.assertArrayEquals
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class FileUtilsTest {

    private static final String CONTENT = "line1" + System.lineSeparator() + "line2";

    @TempDir
    public static Path directoryPath;

    private static File directory;
    private static File content;

    @BeforeAll
    static void prepareTest() throws IOException {
        directory = directoryPath.toFile()

        File a = new File(directory, "a")
        assertTrue(a.createNewFile())

        File b = new File(directory, "b.test")
        assertTrue(b.createNewFile())

        File c = new File(directory, "c.test")
        assertTrue(c.createNewFile())

        content = new File(directory, "content")
        PrintWriter writer = new PrintWriter(content)
        writer.print(CONTENT)
        writer.flush()
        writer.close()
    }

    @Test
    void isIn() {
        assertTrue(FileUtils.contains(directory.listFiles(), "a"))
        assertFalse(FileUtils.contains(directory.listFiles(), "d"))
    }

    @Test
    void getAmountOfFiles() {
        assertEquals(4, FileUtils.getAmountOfFiles(directory))
    }

    @Test
    void getContentOfFile() throws IOException {
        assertEquals(CONTENT, FileUtils.getContentOfFile(content))
    }

    @Test
    void getContentAsLines() throws IOException {
        assertArrayEquals(CONTENT.split(System.lineSeparator()), FileUtils.getContentAsLines(content))
    }

    @Test
    void getFileName() {
        assertEquals("content", FileUtils.getName(content))
        assertEquals("c", FileUtils.getName(new File(directory, "c.test")))
    }

    @Test
    void findFilesByExtension() {
        assertEquals(2, FileUtils.findFilesByExtension(directory, "test").size())
        assertEquals(2, FileUtils.findFilesByExtension(directory.getAbsolutePath(), "test").size())
    }

    @Test
    void collectFiles() {
        assertEquals(4, FileUtils.collectFiles(directory).getChildren().size())
    }

    @Test
    void overrideFile() throws IOException {
        FileUtils.overrideFile(content, "#onlypanda")
        assertEquals("#onlypanda", FileUtils.getContentOfFile(content))
        FileUtils.overrideFile(content, CONTENT)
    }

    @Test
    void delete() throws IOException {
        File d = new File(directory, "d")

        Assertions.assertTrue(d.createNewFile())
        assertEquals(5, FileUtils.getAmountOfFiles(directory))

        assertTrue(FileUtils.delete(d))
        assertEquals(4, FileUtils.getAmountOfFiles(directory))
    }

    @Test
    void toFiles() {
        assertEquals(1, FileUtils.toFiles(content.getAbsolutePath()).length)
    }

}