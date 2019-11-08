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

package org.panda_lang.utilities.annotations.resource;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public final class SystemAnnotationScannerResourceIterator implements Iterator<SystemAnnotationScannerFile> {

    public static long listTime;

    private final SystemAnnotationScannerResource resource;
    private final Stack<Path> stack = new Stack<>();

    public SystemAnnotationScannerResourceIterator(SystemAnnotationScannerResource resource) {
        this.resource = resource;
        stack.addAll(listFiles(resource.file));
    }

    @Override
    public @Nullable SystemAnnotationScannerFile next() {
        Path file = stack.pop();

        if (file == null) {
            return hasNext() ? next() : null;
        }

        return new SystemAnnotationScannerFile(resource, file.toFile());
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    private static List<Path> listFiles(File directory) {
        long time = System.nanoTime();
        List<Path> content = FileUtils.collectPaths(directory.toPath(), 500, path -> path.toString().endsWith(".class"));
        listTime += System.nanoTime() - time;

        return content;
    }

}
