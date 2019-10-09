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

package org.panda_lang.panda.pmm;

import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.utilities.commons.FileUtils;
import org.panda_lang.utilities.commons.IOUtils;
import org.panda_lang.utilities.commons.ZipUtils;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.zip.ZipInputStream;

final class Install implements ThrowingRunnable<IOException> {

    private final PandaModulesManager manager;
    private final ModuleDocument document;

    Install(PandaModulesManager manager, ModuleDocument document) {
        this.manager = manager;
        this.document = document;
    }

    public void run() throws IOException {
        File pandaModules = new File(document.getDocument().getParentFile(), "panda_modules");

        if (!pandaModules.exists()) {
            pandaModules.mkdir();
        }

        Map<String, InstallStatus> statusMap = new HashMap<>();

        for (String dependencyQualifier : document.getDependencies()) {
            Dependency dependency = Dependency.parseDependency(dependencyQualifier);
            statusMap.put(dependency.getName(), install(pandaModules, dependency));
        }

        File[] allModules = Objects.requireNonNull(pandaModules.listFiles());

        for (File moduleDirectory : allModules) {
            scan(statusMap, new File(pandaModules, moduleDirectory.getName()));
        }

        for (Entry<String, InstallStatus> entry : statusMap.entrySet()) {
            manager.getMessenger().sendMessage(MessengerLevel.INFO, entry.getValue().getSymbol() + " " + entry.getKey());
        }
    }

    private InstallStatus install(File pandaModules, Dependency dependency) throws IOException {
        File scopeDirectory = new File(pandaModules, dependency.getScope());
        scopeDirectory.mkdir();
        File moduleDirectory = new File(scopeDirectory, dependency.getName());

        if (moduleDirectory.exists()) {
            File moduleInfoFile = new File(moduleDirectory, "panda.hjson");

            if (!moduleInfoFile.exists()) {
                throw new PandaFrameworkException("Invalid module " + dependency.getName());
            }

            JsonObject moduleInfo = JsonValue.readHjson(FileUtils.getContentOfFile(moduleInfoFile)).asObject();

            if (dependency.getVersion().equals(moduleInfo.getString("version", null))) {
                return InstallStatus.SKIPPED;
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

            return InstallStatus.INSTALLED;
        } finally {
            IOUtils.close(in);
        }
    }

    private void scan(Map<String, InstallStatus> statusMap, File scopeDirectory) {
        File[] modules = Objects.requireNonNull(scopeDirectory.listFiles());

        for (File module : modules) {
            if (statusMap.containsKey(module.getName())) {
                continue;
            }

            FileUtils.delete(module);
            statusMap.put(module.getName(), InstallStatus.REMOVED);
        }
    }

}
