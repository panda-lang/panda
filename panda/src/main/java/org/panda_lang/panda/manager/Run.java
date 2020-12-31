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

import org.panda_lang.language.FrameworkController;
import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.interpreter.source.PandaURLSource;
import org.panda_lang.panda.language.architecture.PandaEnvironment;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.util.Objects;

final class Run {

    private final PackageManager manager;
    private final PackageDocument document;

    Run(PackageManager manager, PackageDocument document) {
        this.manager = manager;
        this.document = document;
    }

    protected Option<Object> run(FrameworkController controller) {
        File mainScript = new File(document.getDocument().getParentFile(), Objects.requireNonNull(document.getMainScript()));

        PandaEnvironment environment = new PandaEnvironment(controller, manager.getWorkingDirectory());
        environment.initialize();

        File pandaModules = document.getPandaModules();
        File[] owners = pandaModules.listFiles();

        for (File ownerDirectory : owners) {
            for (File moduleDirectory : Objects.requireNonNull(ownerDirectory.listFiles())) {
                if (!new File(moduleDirectory, PackageManagerConstants.PACKAGE_INFO).exists()) {
                    manager.getLogger().debug("Module located in " + moduleDirectory + " does not contain package info");
                    continue;
                }

                PackageManagerUtils.loadToEnvironment(environment, moduleDirectory).onError(error -> {
                    throw new PandaFrameworkException("Cannot load package: " + error);
                });
            }
        }

        return environment.getInterpreter()
                .interpret(PandaURLSource.fromFile(mainScript))
                .orElseThrow(throwable -> new RuntimeException("Cannot launch application due to failures in interpretation process"))
                .launch()
                .get();
    }

}
