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
import org.panda_lang.utilities.commons.function.ThrowingRunnable;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class Install implements ThrowingRunnable<IOException> {

    private final PackageManager manager;
    private final PackageDocument document;

    Install(PackageManager manager, PackageDocument document) {
        this.manager = manager;
        this.document = document;
    }

    public void run() throws IOException {
        manager.getMessenger().send(Level.DEBUG, "--- Installing dependencies");

        Dependency documentDependency = document.toDependency();
        File pandaModules = new File(document.getDocument().getParentFile(), PackageManagerConstants.MODULES);

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
        File ownerDirectory = new File(pandaModules, dependency.getOwner());
        ownerDirectory.mkdir();

        File packageDirectory = new File(ownerDirectory, dependency.getName());
        File packageInfoFile = new File(packageDirectory, PackageManagerConstants.PACKAGE_INFO);

        if (packageDirectory.exists()) {
            if (!packageInfoFile.exists()) {
                throw new PandaFrameworkException("Invalid package " + dependency.getName());
            }

            PackageDocument packageInfo = new PackageDocumentFile(packageInfoFile).getContent();

            if (dependency.getVersion().equals(packageInfo.getVersion())) {
                log(InstallStatus.SKIPPED, dependency);
                return;
            }

            FileUtils.delete(packageDirectory);
        }

        CustomInstallFactory customInstallFactory = new CustomInstallFactory();
        CustomInstall customInstall = customInstallFactory.createCustomInstall(dependency);

        List<Dependency> dependencies = customInstall.install(this::log, ownerDirectory, packageInfoFile);
        dependenciesToLoad.addAll(dependencies);
    }

    protected void scan(Map<String, Dependency> dependenciesMap, File ownerDirectory) {
        File[] modules = Objects.requireNonNull(ownerDirectory.listFiles());

        for (File module : modules) {
            if (dependenciesMap.containsKey(module.getName())) {
                continue;
            }

            FileUtils.delete(module);
            log(InstallStatus.REMOVED, dependenciesMap.remove(module.getName()));
        }
    }

    protected void log(InstallStatus status, Dependency dependency) {
        manager.getMessenger().send(Level.DEBUG, status.getSymbol() + " " + dependency.toString());
    }

}
