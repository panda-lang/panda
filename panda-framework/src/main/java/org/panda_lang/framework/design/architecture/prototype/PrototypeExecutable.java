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

package org.panda_lang.framework.design.architecture.prototype;

import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.runtime.ProcessStack;

public interface PrototypeExecutable {

    <T> T invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception;

    /**
     * Get associated prototype
     *
     * @return the associated prototype
     */
    PrototypeReference getPrototype();

    /**
     * Get visibility of executable
     *
     * @return the visibility
     */
    PrototypeVisibility getVisibility();

    /**
     * Get type references of executable's parameters
     *
     * @return array of used by parameter types
     */
    PrototypeReference[] getParameterTypes();

    /**
     * Get parameters used by executable
     *
     * @return array of used parameters
     */
    Parameter[] getParameters();

    /**
     * Get return type of executable
     *
     * @return the return type reference
     */
    PrototypeReference getReturnType();

    /**
     * Get name of prototype
     *
     * @return the name
     */
    String getName();

}
