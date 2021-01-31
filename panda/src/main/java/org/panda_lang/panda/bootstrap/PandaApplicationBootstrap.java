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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.framework.architecture.Application;
import org.panda_lang.panda.Panda;
import org.panda_lang.utilities.commons.function.Result;

import java.io.File;

/**
 * Simplified application loader
 */
public final class PandaApplicationBootstrap {

    protected final Panda panda;
    protected String script;
    protected File directory;

    PandaApplicationBootstrap(Panda panda) {
        this.panda = panda;
    }

    /**
     * Define main script of application
     *
     * @param script the main script to use
     * @return the bootstrap instance
     */
    public PandaApplicationBootstrap script(String script) {
        this.script = script;
        return this;
    }

    /**
     * Set working directory of application
     *
     * @param workingDirectory the directory to use
     * @return the bootstrap instance
     */
    public PandaApplicationBootstrap workingDirectory(String workingDirectory) {
        return workingDirectory(new File(workingDirectory));
    }

    /**
     * Set working directory of application
     *
     * @param workingDirectory the directory to use
     * @return the bootstrap instance
     */
    public PandaApplicationBootstrap workingDirectory(File workingDirectory) {
        this.directory = workingDirectory;
        return this;
    }

    /**
     * Load application using the collected data
     *
     * @return loaded application or nothing if something happen
     */
    public Result<Application, Throwable> createApplication() {
        return panda.getLoader().load(script, directory);
    }

    /**
     * Create application bootstrap
     *
     * @param panda instance of panda to use
     * @return a new application bootstrap instance
     */
    public static PandaApplicationBootstrap create(Panda panda) {
        return new PandaApplicationBootstrap(panda);
    }

}
