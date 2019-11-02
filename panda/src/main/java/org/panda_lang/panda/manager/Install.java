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

package org.panda_lang.panda.manager;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.utilities.commons.FileUtils;
import org.panda_lang.utilities.commons.IOUtils;
import org.panda_lang.utilities.commons.ZipUtils;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;
import org.slf4j.event.Level;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

final class Install implements ThrowingRunnable<IOException> {

    private final ModuleManager manager;
    private final ModuleDocument document;

    Install(ModuleManager manager, ModuleDocument document) {
        this.manager = manager;
        this.document = document;
    }

    public void run() throws IOException {
        manager.getMessenger().send(Level.DEBUG, "--- Installing dependencies");

        Dependency documentDependency = document.toDependency();
        File pandaModules = new File(document.getDocument().getParentFile(), "panda_modules");

        if (!pandaModules.exists()) {
            pandaModules.mkdir();
        }

        Map<String, Dependency> dependencyMap = new HashMap<>();
        Collection<Dependency> dependencies = new ArrayList<>(document.getDependencies());

        while (!dependencies.isEmpty()) {
            List<Dependency> dependenciesToLoad = new ArrayList<>();

            for (Dependency dependency : dependencies) {
                if (documentDependency.equals(dependency)) {
                    manager.getMessenger().send(Level.WARN, "Module contains circular dependency to itself");
                    continue;
                }

                String name = dependency.getName();

                if (dependencyMap.containsKey(name) && dependencyMap.get(name).hasHigherVersion(dependency.getVersion())) {
                    continue;
                }

                install(pandaModules, dependency, dependenciesToLoad);
                dependencyMap.put(name, dependency);
            }

            dependencies.clear();
            dependencies.addAll(dependenciesToLoad);
        }

        File[] allModules = Objects.requireNonNull(pandaModules.listFiles());

        for (File moduleDirectory : allModules) {
            scan(dependencyMap, new File(pandaModules, moduleDirectory.getName()));
        }

        manager.getMessenger().send(Level.DEBUG, "");
    }

    private void install(File pandaModules, Dependency dependency, Collection<Dependency> dependenciesToLoad) throws IOException {
        File scopeDirectory = new File(pandaModules, dependency.getScope());
        scopeDirectory.mkdir();

        File moduleDirectory = new File(scopeDirectory, dependency.getName());
        File moduleInfoFile = new File(moduleDirectory, "panda.hjson");

        if (moduleDirectory.exists()) {
            if (!moduleInfoFile.exists()) {
                throw new PandaFrameworkException("Invalid module " + dependency.getName());
            }

            ModuleDocument moduleInfo = new ModuleDocumentFile(moduleInfoFile).getContent();

            if (dependency.getVersion().equals(moduleInfo.getVersion())) {
                log(InstallStatus.SKIPPED, dependency);
                return;
            }

            FileUtils.delete(moduleDirectory);
        }

        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new URL(dependency.getAddress()).openStream());
            ZipInputStream zipStream = new ZipInputStream(in);
            ZipUtils.extract(zipStream, scopeDirectory);

            File extractedModule = new File(scopeDirectory, dependency.getName() + "-" + dependency.getVersion());
            extractedModule.renameTo(moduleDirectory);

            ModuleDocument moduleInfo = new ModuleDocumentFile(moduleInfoFile).getContent();
            dependenciesToLoad.addAll(moduleInfo.getDependencies());
            log(InstallStatus.INSTALLED, dependency);
        } finally {
            IOUtils.close(in);
        }
    }

    private void scan(Map<String, Dependency> dependenciesMap, File scopeDirectory) {
        File[] modules = Objects.requireNonNull(scopeDirectory.listFiles());

        for (File module : modules) {
            if (dependenciesMap.containsKey(module.getName())) {
                continue;
            }

            FileUtils.delete(module);
            log(InstallStatus.REMOVED, dependenciesMap.remove(module.getName()));
        }
    }

    private void log(InstallStatus status, Dependency dependency) {
        manager.getMessenger().send(Level.DEBUG, status.getSymbol() + " " + dependency.toString());
    }

}
