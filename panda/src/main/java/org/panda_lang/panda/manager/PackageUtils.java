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

import net.dzikoysk.cdn.CdnFactory;
import org.panda_lang.framework.architecture.module.Module;
import org.panda_lang.framework.architecture.module.ModuleSource;
import org.panda_lang.framework.architecture.module.PandaModule;
import org.panda_lang.framework.architecture.packages.Package;
import org.panda_lang.framework.interpreter.source.Source;
import org.panda_lang.framework.interpreter.source.URLSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class PackageUtils {

    private PackageUtils() {}

    public static Package scriptToPackage(File script) throws Exception {
        if (!script.exists()) {
            throw new IllegalStateException("Provided file does not exist");
        }

        if (!script.isFile()) {
            throw new IllegalArgumentException("Provided file is not a file");
        }

        if (script.getName().endsWith("panda.cdn")) {
            return directoryToPackage(script.getParentFile());
        }

        PackageDocument packageDocument = new PackageDocument();
        Package pkg = new Package(packageDocument.name, packageDocument.author, packageDocument.version, script);
        PandaModule defaultModule = new PandaModule(pkg, Package.DEFAULT_MODULE);
        ModuleSource moduleSource = new ModuleSource(defaultModule, Collections.singleton(URLSource.fromFile(defaultModule, script)));
        pkg.addModule(moduleSource);

        return pkg;
    }

    public static Package directoryToPackage(File directory) throws Exception {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Provided file is not a directory: " + directory);
        }

        File packageDocumentFile = new File(directory, PackageManagerConstants.PACKAGE_INFO);

        if (!packageDocumentFile.exists()) {
            throw new IllegalStateException("Package description panda.cdn does not exist");
        }

        PackageDocument packageDocument = CdnFactory.createStandard().load(packageDocumentFile, PackageDocument.class);
        Package pkg = new Package(packageDocument.name, packageDocument.author, packageDocument.version, directory);
        File sources = new File(directory, packageDocument.sources);

        if (!sources.exists()) {
            throw new IllegalStateException("Missing sources directory: " + sources);
        }

        directoriesToModuleSources(pkg, Package.DEFAULT_MODULE, sources).forEach(pkg::addModule);

        return pkg;
    }

    private static Collection<ModuleSource> directoriesToModuleSources(Package pkg, String currentName, File currentDirectory) {
        Collection<ModuleSource> moduleSources = new ArrayList<>();

        File[] files = currentDirectory.listFiles();
        Module module = new PandaModule(pkg, currentName);

        Collection<? extends Source> sources = Arrays.stream(files)
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".panda"))
                .map(file -> URLSource.fromFile(module, file))
                .collect(Collectors.toList());

        ModuleSource moduleSource = new ModuleSource(module, sources);
        moduleSources.add(moduleSource);

        Arrays.stream(files)
                .filter(File::isDirectory)
                .filter(directory -> !directory.getName().equalsIgnoreCase(PackageManagerConstants.MODULES))
                .map(directory -> directoriesToModuleSources(pkg, currentName.isEmpty() ? directory.getName() : module.getSimpleName() + ":" + directory.getName(), directory))
                .forEach(moduleSources::addAll);

        return moduleSources;
    }

}
