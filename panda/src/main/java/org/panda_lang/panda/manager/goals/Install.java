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

package org.panda_lang.panda.manager.goals;

import org.panda_lang.panda.manager.Dependency;
import org.panda_lang.panda.manager.PackageInfo;
import org.panda_lang.panda.manager.PackageManager;
import org.panda_lang.panda.manager.PackageManagerConstants;
import panda.utilities.FileUtils;
import panda.std.function.ThrowingRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Install implements ThrowingRunnable<Exception> {

    private final PackageManager packageManager;
    private final PackageInfo document;

    public Install(PackageManager packageManager, PackageInfo document) {
        this.packageManager = packageManager;
        this.document = document;
    }

    public void run() throws Exception {
        packageManager.getLogger().debug("--- Installing dependencies");

        Dependency documentDependency = document.toDependency();
        File pandaModules = new File(document.getFile().getParentFile(), PackageManagerConstants.MODULES);

        if (!pandaModules.exists()) {
            pandaModules.mkdir();
        }

        Map<String, Dependency> dependencyMap = new HashMap<>();
        Collection<Dependency> dependencies = new ArrayList<>(document.getDependencies());

        while (!dependencies.isEmpty()) {
            List<Dependency> dependenciesToLoad = new ArrayList<>();

            for (Dependency dependency : dependencies) {
                if (documentDependency.same(dependency)) {
                    packageManager.getLogger().warn("Module contains circular dependency to itself");
                    continue;
                }

                String identifier = dependency.getIdentifier();

                if (dependencyMap.containsKey(identifier) && dependencyMap.get(identifier).hasHigherVersion(dependency.getVersion())) {
                    continue;
                }

                install(pandaModules, dependency, dependenciesToLoad);
                dependencyMap.put(identifier, dependency);
            }

            dependencies.clear();
            dependencies.addAll(dependenciesToLoad);
        }

        File[] allModules = Objects.requireNonNull(pandaModules.listFiles());

        for (File moduleDirectory : allModules) {
            scan(dependencyMap, new File(pandaModules, moduleDirectory.getName()));
        }

        packageManager.getLogger().debug("");
    }

    private void install(File pandaModules, Dependency dependency, Collection<Dependency> dependenciesToLoad) throws Exception {
        File ownerDirectory = new File(pandaModules, dependency.getAuthor());
        ownerDirectory.mkdir();

        File packageDirectory = new File(ownerDirectory, dependency.getName());
        File packageInfoFile = new File(packageDirectory, PackageManagerConstants.PACKAGE_INFO);

        if (packageDirectory.exists() && packageInfoFile.exists()) {
            PackageInfo packageInfo = packageManager.readPackageInfo(packageInfoFile);

            if (dependency.getVersion().equals(packageInfo.getDocument().version)) {
                log(InstallStatus.SKIPPED, dependency);
                return;
            }

            FileUtils.delete(packageDirectory);
        }

        if (!packageInfoFile.exists()) {
            packageManager.getLogger().debug("Dependency " + dependency.getName() + " does not contain valid package document");
        }

        CustomInstallFactory customInstallFactory = new CustomInstallFactory();
        CustomInstall customInstall = customInstallFactory.createCustomInstall(packageManager, document, dependency);

        List<Dependency> dependencies = customInstall.install(this::log, ownerDirectory, packageInfoFile);
        dependenciesToLoad.addAll(dependencies);
    }

    protected void scan(Map<String, Dependency> dependenciesMap, File authorDirectory) {
        File[] packages = Objects.requireNonNull(authorDirectory.listFiles());

        for (File pkg : packages) {
            String identifier = authorDirectory.getName() + "/" + pkg.getName();

            if (dependenciesMap.containsKey(identifier)) {
                continue;
            }

            FileUtils.delete(pkg);
            log(InstallStatus.REMOVED, dependenciesMap.remove(identifier));
        }
    }

    protected void log(InstallStatus status, Dependency dependency) {
        packageManager.getLogger().debug(status.getSymbol() + " " + dependency.toString());
    }

}
