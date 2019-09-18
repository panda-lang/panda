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

package org.panda_lang.framework.design.architecture;

import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.interpreter.Interpreter;
import org.panda_lang.framework.design.resource.Resources;

import java.io.File;

public interface Environment {

    /**
     * Get module path of environment
     *
     * @return the module path
     */
    ModulePath getModulePath();

    /**
     * Get interpreter of environment
     *
     * @return the interpreter
     */
    Interpreter getInterpreter();

    /**
     * Get resources assigned to the environment
     *
     * @return the resources
     */
    Resources getResources();

    /**
     * Get working directory
     *
     * @return the working directory
     */
    File getDirectory();

}
