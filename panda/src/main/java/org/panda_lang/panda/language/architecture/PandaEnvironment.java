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

package org.panda_lang.panda.language.architecture;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.framework.language.architecture.module.PandaTypeLoader;
import org.panda_lang.framework.language.interpreter.messenger.PandaMessenger;
import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.language.interpreter.PandaInterpreter;
import org.panda_lang.panda.language.resource.ResourcesLoader;
import org.slf4j.Logger;

import java.io.File;

public final class PandaEnvironment implements Environment {

    private final FrameworkController controller;
    private final File workingDirectory;
    private final Messenger messenger;
    private final ModulePath modulePath;
    private final TypeLoader typeLoader;
    private final PandaInterpreter interpreter;
    private boolean initialized;

    public PandaEnvironment(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
        this.modulePath = new PandaModulePath();
        this.typeLoader = new PandaTypeLoader();
        this.messenger = new PandaMessenger(controller);
        this.interpreter = new PandaInterpreter(this);
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        this.initialized = true;
        controller.getResources().getOutputListener().peek(messenger::setOutputListener);

        ResourcesLoader resourcesLoader = new ResourcesLoader();
        resourcesLoader.load(modulePath, typeLoader);
    }

    @Override
    public PandaInterpreter getInterpreter() {
        if (!initialized) {
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
    public Messenger getMessenger() {
        return messenger;
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
