/*
 * Copyright (c) 2015-2020 Dzikoysk
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
import org.panda_lang.utilities.commons.ZipUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.zip.ZipInputStream;

final class GitHubInstall implements CustomInstall {

    private final Dependency dependency;

    GitHubInstall(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public List<Dependency> install(BiConsumer<InstallStatus, Dependency> resultConsumer, File ownerDirectory, File packageInfoFile) throws IOException {
        File packageDirectory = new File(ownerDirectory, dependency.getName());
        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new URL(dependency.getAddress()).openStream());
            ZipInputStream zipStream = new ZipInputStream(in);
            ZipUtils.extract(zipStream, ownerDirectory);

            File extractedModule = new File(ownerDirectory, dependency.getName() + "-" + dependency.getVersion());
            extractedModule.renameTo(packageDirectory);
            resultConsumer.accept(InstallStatus.INSTALLED, dependency);

            PackageDocument packageInfo = new PackageDocumentFile(packageInfoFile).getContent();
            return packageInfo.getDependencies();
        } finally {
            IOUtils.close(in);
        }
    }

}
