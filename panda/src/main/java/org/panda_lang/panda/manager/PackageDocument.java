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

package org.panda_lang.panda.manager;

import net.dzikoysk.cdn.CDN;
import net.dzikoysk.cdn.model.Configuration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

final class PackageDocument {

    private final File document;
    private final Configuration content;

    PackageDocument(File document, String source) {
        this.document = document;
        this.content = CDN.defaultInstance().parse(source);
    }

    protected Dependency toDependency() {
        return new Dependency("", getOwner(), getName(), getVersion());
    }

    private List<Dependency> getDependencies(String name) {
        DependencyFactory factory = new DependencyFactory();

        return content.getList(name, Collections.emptyList()).stream()
                .map(factory::createDependency)
                .collect(Collectors.toList());
    }

    protected List<? extends String> getRepositories() {
        return content.getList("repositories", Collections.emptyList());
    }

    protected List<Dependency> getTestsDependencies() {
        return getDependencies("tests-dependencies");
    }

    protected List<Dependency> getDependencies() {
        return getDependencies("dependencies");
    }

    protected @Nullable String getMainScript() {
        return content.getString("scripts.main").get();
    }

    protected String getOwner() {
        return content.getString("owner").get();
    }

    protected String getVersion() {
        return content.getString("version").get();
    }

    protected String getName() {
        return content.getString("name").get();
    }

    protected File getPandaModules() {
        return new File(getDocument().getParent(), PackageManagerConstants.MODULES);
    }

    protected File getDocument() {
        return document;
    }

}
