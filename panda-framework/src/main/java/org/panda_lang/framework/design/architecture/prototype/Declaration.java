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

import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.interpreter.source.Source;

import java.util.Collection;

public interface Declaration extends Property {

    /**
     * Get reference to the array type of this type
     *
     * @param moduleLoader
     * @return the reference to array type
     */
    Reference toArray(ModuleLoader moduleLoader);

    /**
     * Inherit the given reference
     *
     * @param reference the reference to inherit from
     */
    void addSuper(Reference reference);

    boolean isClassOf(String className);

    boolean isAssignableFrom(Declaration prototype);

    Collection<? extends Reference> getSupers();

    Class<?> getAssociatedClass();

    State getState();

    Source getSource();

}
