/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter;

import org.panda_lang.framework.interpreter.SourceFile;
import org.panda_lang.framework.interpreter.SourceSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PandaSourceSet implements SourceSet {

    private final Collection<SourceFile> sourceFiles;

    public PandaSourceSet() {
        this.sourceFiles = new ArrayList<>();
    }

    public SourceFile add(File file) {
        SourceFile sourceFile = new PandaSourceFile(file);
        sourceFiles.add(sourceFile);
        return sourceFile;
    }

    @Override
    public Collection<String> getSources() {
        Collection<String> sources = new ArrayList<>();

        for (SourceFile sourceFile : getSourceFiles()) {
            sources.add(sourceFile.getContent());
        }

        return sources;
    }

    @Override
    public Collection<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

}
