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

package org.panda_lang.panda.utilities.commons.io;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.collection.TreeNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUtils {

    /**
     * Check if a file is in the specified array
     *
     * @param files    files
     * @param fileName name of file
     * @return true if file is in specified array
     */
    public static boolean isIn(File[] files, String fileName) {
        if (files == null) {
            return false;
        }

        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Count files in the specified directory
     *
     * @param directory root directory
     * @return amount of files
     */
    public static int getAmountOfFiles(File directory) {
        if (!directory.isDirectory()) {
            return 0;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return 0;
        }

        return files.length;
    }

    /**
     * @return content of the specified file
     */
    public static String getContentOfFile(File file) {
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Can't read contents of file: " + file, e);
        }
    }

    /**
     * @return content of file divided by lines
     */
    public static @Nullable String[] getContentAsLines(File file) {
        if (!file.exists()) {
            return new String[0];
        }

        try {
            List<String> list = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            String[] result = new String[list.size()];

            return list.toArray(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return file name without extension
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");

        if (pos == -1) {
            return fileName;
        }

        return fileName.substring(0, pos);
    }

    /**
     * @return collection of file with the specified extension
     */
    public static Collection<File> findFilesByExtension(String directory, String extension) {
        return findFilesByExtension(new File(directory), extension);
    }

    /**
     * @return collection of file with the specified extension
     */
    public static Collection<File> findFilesByExtension(File directory, String extension) {
        Collection<File> files = new ArrayList<>();
        findFilesByExtension(directory, extension, files);
        return files;
    }

    private static void findFilesByExtension(File directory, String extension, Collection<File> files) {
        if (directory.isFile()) {
            return;
        }

        File[] filesList = directory.listFiles();

        if (filesList == null) {
            return;
        }

        for (File file : filesList) {
            if (file.isDirectory()) {
                findFilesByExtension(file, extension, files);
                continue;
            }

            if (!file.getName().endsWith(extension)) {
                continue;
            }

            files.add(file);
        }
    }

    /**
     * Collect all files and subfiles in the specified directory
     *
     * @param directory root
     * @return tree node of collected files
     */
    public static TreeNode<File> collectFiles(File directory) {
        TreeNode<File> tree = new TreeNode<>(directory);

        if (!directory.isDirectory()) {
            return tree;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return tree;
        }

        for (File file : files) {
            tree.add(file.isDirectory() ? collectFiles(file) : new TreeNode<>(file));
        }

        return tree;
    }

    /**
     * Override content of the specified file
     */
    public static void overrideFile(File file, String content) {
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Can't write contents to file: " + file, e);
        }
    }

    /**
     * Delete file or directory with content
     *
     * @param file file/directory
     * @return true if succeeded
     */
    public static boolean delete(File file) {
        if (!file.exists()) {
            return true;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (File c : files) {
                    delete(c);
                }
            }
        }

        return file.delete();
    }

    /**
     * Get vararg paths as array of {@link File}
     *
     * @param paths array of paths
     * @return array of files
     */
    public static File[] toFiles(String... paths) {
        File[] files = new File[paths.length];

        for (int i = 0; i < files.length; i++) {
            files[i] = new File(paths[i]);
        }

        return files;
    }

}
