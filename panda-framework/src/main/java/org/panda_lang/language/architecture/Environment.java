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

package org.panda_lang.language.architecture;

import org.panda_lang.language.FrameworkController;
import org.panda_lang.language.architecture.module.ModulePath;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.interpreter.Interpreter;
import org.panda_lang.language.interpreter.logging.LoggerHolder;
import org.panda_lang.language.interpreter.source.SourceService;

import java.io.File;

/**
 * Application playground
 */
public interface Environment extends LoggerHolder {

    /**
     * Get interpreter of environment
     *
     * @return the interpreter
     */
    Interpreter getInterpreter();

    /**
     * Get module path of environment
     *
     * @return the module path
     */
    ModulePath getModulePath();

    /**
     * Get assigned to the environment type loader
     *
     * @return the type loader
     */
    TypeLoader getTypeLoader();

    /**
     * Get sources used by this environment
     *
     * @return the associated set of sources
     */
    SourceService getSources();

    /**
     * Get working directory
     *
     * @return the working directory
     */
    File getDirectory();

    /**
     * Get framework controller
     *
     * @return the controller
     */
    FrameworkController getController();

}
