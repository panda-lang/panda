/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.manager;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public final class PackageInfo {

    private final File file;
    private final PackageDocument document;

    PackageInfo(File file, PackageDocument document) {
        this.file = file;
        this.document = document;
    }

    public Dependency toDependency() {
        return new Dependency("unknown", document.name, document.author, document.version);
    }

    public List<Dependency> getDependencies() {
        return document.dependencies.stream()
                .map(Dependency::createDependency)
                .collect(Collectors.toList());
    }

    public File getPandaModules() {
        return new File(file.getParent(), PackageManagerConstants.MODULES);
    }

    public PackageDocument getDocument() {
        return document;
    }

    public File getFile() {
        return file;
    }

}
