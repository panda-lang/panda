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

package org.panda_lang.framework.language.resource.internal;

import org.panda_lang.framework.design.architecture.module.Module;

/**
 * Supports simplified mapping of Java classes
 */
public interface InternalModuleInfo {

    /**
     * Custom initializer of module
     *
     * @param module current module
     */
    default void initialize(Module module) { }

    /**
     * Get names of classes in the given package to map
     *
     * @return names to map
     */
    String[] getNames();

    /**
     * Get associated with the module package
     *
     * @return the name of package
     */
    String getPackageName();

    /**
     * Get the name of represented module
     *
     * @return the name of module
     */
    String getModule();

}
