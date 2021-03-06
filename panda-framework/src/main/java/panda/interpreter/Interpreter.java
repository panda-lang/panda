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

import panda.interpreter.architecture.Application;
import panda.interpreter.architecture.Environment;
import panda.interpreter.architecture.packages.Package;
import panda.std.Result;

/**
 * Translate source code into efficient intermediate representation and build an application
 */
public interface Interpreter {

    /**
     * Starts the process of interpretation
     *
     * @return interpreted application
     */
    Result<Application, Throwable> interpret(Package packageSource);

    /**
     * Get associated environment
     *
     * @return the environment
     */
    Environment getEnvironment();

}