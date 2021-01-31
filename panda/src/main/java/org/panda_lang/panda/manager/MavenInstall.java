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

import org.panda_lang.utilities.commons.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

final class MavenInstall implements CustomInstall {

    private final PackageManager manager;
    private final PackageDocument document;
    private final Dependency dependency;

    public MavenInstall(PackageManager manager, PackageDocument document, Dependency dependency) {
        this.manager = manager;
        this.document = document;
        this.dependency = dependency;
    }

    @Override
    public List<Dependency> install(BiConsumer<InstallStatus, Dependency> resultConsumer, File ownerDirectory, File packageInfoFile) throws IOException {
        List<? extends String> repositories = document.getRepositories();

        String gav = dependency.getOwner().replace(".", "/")
                + "/" + dependency.getName()
                + "/" + dependency.getVersion()
                + "/" + dependency.getName() + "-" + dependency.getVersion() + ".jar";

        if (repositories.isEmpty()) {
            throw new IllegalStateException("Panda cannot download maven dependency without defined any repository");
        }

        for (String repository : repositories) {
            String address = repository;

            if (!address.endsWith("/")) {
                address += "/";
            }

            address += gav;

            InputStream in = null;

            try {
                in = IOUtils.fetchContentAsStream(address).orElseThrow(ioException -> ioException);

                File dependencyDirectory = new File(ownerDirectory, dependency.getName());
                dependencyDirectory.mkdirs();

                File dependencyFile = new File(dependencyDirectory, dependency.getName() + "-" + dependency.getVersion() + ".jar");
                Files.copy(in, dependencyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

//                File dependencyDocument = new File(dependencyDirectory, "panda.cdn");
//                FileUtils.overrideFile(dependencyDocument, "name: " + dependency.getName() + "\nversion: " + dependency.getVersion());

                manager.getFrameworkController().getClassLoader().addURL(dependencyFile.toURI().toURL());
            } finally {
                IOUtils.close(in);
            }
        }

        resultConsumer.accept(InstallStatus.INSTALLED, dependency);
        return Collections.emptyList();
    }

}
