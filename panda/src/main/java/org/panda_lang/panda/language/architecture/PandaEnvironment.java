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

package org.panda_lang.panda.language.architecture;

import org.panda_lang.language.FrameworkController;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.module.ModulePath;
import org.panda_lang.language.architecture.module.PandaModulePath;
import org.panda_lang.language.architecture.module.PandaTypeLoader;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.interpreter.logging.Logger;
import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.language.interpreter.PandaInterpreter;
import org.panda_lang.panda.language.resource.ResourcesLoader;
import org.panda_lang.utilities.commons.function.Lazy;

import java.io.File;

public final class PandaEnvironment implements Environment {

    private final FrameworkController controller;
    private final File workingDirectory;
    private final ModulePath modulePath;
    private final TypeLoader typeLoader;
    private final PandaInterpreter interpreter;
    private final Lazy<Void> resources;

    public PandaEnvironment(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
        this.modulePath = new PandaModulePath();
        this.typeLoader = new PandaTypeLoader(controller);
        this.interpreter = new PandaInterpreter(this);
        this.resources = new Lazy<>(() -> {
            ResourcesLoader resourcesLoader = new ResourcesLoader();
            resourcesLoader.load(modulePath, typeLoader);
        });
    }

    public synchronized void initialize() {
        resources.get();
    }

    @Override
    public PandaInterpreter getInterpreter() {
        if (!resources.isInitialized()) {
            throw new PandaException("Environment was not initialized");
        }

        return interpreter;
    }

    @Override
    public Logger getLogger() {
        return getController().getLogger();
    }

    @Override
    public ModulePath getModulePath() {
        return modulePath;
    }

    @Override
    public TypeLoader getTypeLoader() {
        return typeLoader;
    }

    @Override
    public File getDirectory() {
        return workingDirectory;
    }

    @Override
    public FrameworkController getController() {
        return controller;
    }

}
