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

package panda.interpreter;

import panda.interpreter.architecture.Environment;
import panda.interpreter.architecture.module.PandaTypeLoader;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.packages.Packages;
import panda.interpreter.architecture.type.generator.TypeGenerator;
import panda.interpreter.logging.Logger;
import panda.interpreter.source.SourceService;
import panda.interpreter.std.StdLoader;
import panda.std.Lazy;

import java.io.File;

public final class PandaEnvironment implements Environment {

    private final FrameworkController controller;
    private final File workingDirectory;
    private final SourceService sources;
    private final Packages packages;
    private final TypeGenerator typeGenerator;
    private final TypeLoader typeLoader;
    private final PandaInterpreter interpreter;
    private final Lazy<Void> std;

    public PandaEnvironment(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
        this.sources = new SourceService();
        this.packages = new Packages(sources);
        this.typeLoader = new PandaTypeLoader(packages);
        this.typeGenerator = new TypeGenerator(controller);
        this.interpreter = new PandaInterpreter(this);

        this.std = Lazy.ofRunnable(() -> {
            StdLoader stdLoader = new StdLoader();
            stdLoader.load(packages, typeGenerator, typeLoader);
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
    public Packages getPackages() {
        return packages;
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
