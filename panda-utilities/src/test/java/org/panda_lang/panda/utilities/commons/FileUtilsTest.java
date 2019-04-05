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

package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

class FileUtilsTest {

    private static final String CONTENT = "line1" + System.lineSeparator() + "line2";

    @TempDir
    public static Path directoryPath;

    private static File directory;
    private static File content;

    @BeforeAll
    static void prepareTest() throws IOException {
        directory = directoryPath.toFile();

        File a = new File(directory, "a");
        Assertions.assertTrue(a.createNewFile());

        File b = new File(directory, "b.test");
        Assertions.assertTrue(b.createNewFile());

        File c = new File(directory, "c.test");
        Assertions.assertTrue(c.createNewFile());

        content = new File(directory, "content");
        PrintWriter writer = new PrintWriter(content);
        writer.print(CONTENT);
        writer.flush();
        writer.close();
    }

    @Test
    void isIn() {
        Assertions.assertTrue(FileUtils.isIn(directory.listFiles(), "a"));
        Assertions.assertFalse(FileUtils.isIn(directory.listFiles(), "d"));
    }

    @Test
    void getAmountOfFiles() {
        Assertions.assertEquals(4, FileUtils.getAmountOfFiles(directory));
    }

    @Test
    void getContentOfFile() {
        Assertions.assertEquals(CONTENT, FileUtils.getContentOfFile(content));
    }

    @Test
    void getContentAsLines() {
        Assertions.assertArrayEquals(CONTENT.split(System.lineSeparator()), FileUtils.getContentAsLines(content));
    }

    @Test
    void getFileName() {
        Assertions.assertEquals("content", FileUtils.getFileName(content));
        Assertions.assertEquals("c", FileUtils.getFileName(new File(directory, "c.test")));
    }

    @Test
    void findFilesByExtension() {
        Assertions.assertEquals(2, FileUtils.findFilesByExtension(directory, "test").size());
        Assertions.assertEquals(2, FileUtils.findFilesByExtension(directory.getAbsolutePath(), "test").size());
    }

    @Test
    void collectFiles() {
        Assertions.assertEquals(4, FileUtils.collectFiles(directory).getChildren().size());
    }

    @Test
    void overrideFile() {
        FileUtils.overrideFile(content, "#onlypanda");
        Assertions.assertEquals("#onlypanda", FileUtils.getContentOfFile(content));
        FileUtils.overrideFile(content, CONTENT);
    }

    @Test
    void delete() throws IOException {
        File d = new File(directory, "d");

        Assertions.assertTrue(d.createNewFile());
        Assertions.assertEquals(5, FileUtils.getAmountOfFiles(directory));

        Assertions.assertTrue(FileUtils.delete(d));
        Assertions.assertEquals(4, FileUtils.getAmountOfFiles(directory));
    }

    @Test
    void toFiles() {
        Assertions.assertEquals(1, FileUtils.toFiles(content.getAbsolutePath()).length);
    }

}