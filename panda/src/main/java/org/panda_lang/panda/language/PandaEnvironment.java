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

package org.panda_lang.panda.language;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.architecture.Environment;
import org.panda_lang.framework.architecture.module.ModulePath;
import org.panda_lang.framework.architecture.module.PandaModulePath;
import org.panda_lang.framework.architecture.module.PandaTypeLoader;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.type.generator.TypeGenerator;
import org.panda_lang.framework.interpreter.logging.Logger;
import org.panda_lang.framework.interpreter.source.PandaSourceService;
import org.panda_lang.framework.interpreter.source.SourceService;
import org.panda_lang.panda.language.std.StdLoader;
import org.panda_lang.utilities.commons.function.Lazy;

import java.io.File;

public final class PandaEnvironment implements Environment {

    private final FrameworkController controller;
    private final File workingDirectory;
    private final SourceService sources;
    private final ModulePath modulePath;
    private final TypeGenerator typeGenerator;
    private final TypeLoader typeLoader;
    private final PandaInterpreter interpreter;
    private final Lazy<Void> std;

    public PandaEnvironment(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
        this.sources = new PandaSourceService();
        this.modulePath = new PandaModulePath(sources);
        this.typeLoader = new PandaTypeLoader(modulePath);
        this.typeGenerator = new TypeGenerator(controller);
        this.interpreter = new PandaInterpreter(this);

        this.std = new Lazy<>(() -> {
            StdLoader stdLoader = new StdLoader();
            stdLoader.load(modulePath, typeGenerator, typeLoader);
        });
    }

    public synchronized void initialize() {
        std.get();
    }

    @Override
    public PandaInterpreter getInterpreter() {
        if (!std.isInitialized()) {
            throw new PandaFrameworkException("Environment was not initialized");
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
    public TypeGenerator getTypeGenerator() {
        return typeGenerator;
    }

    @Override
    public TypeLoader getTypeLoader() {
        return typeLoader;
    }

    @Override
    public SourceService getSources() {
        return sources;
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
