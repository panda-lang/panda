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

package org.panda_lang.panda.framework.design.architecture.module;

import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;
import java.util.Optional;

public interface ModulePath {

    String DEFAULT_MODULE = null;

    Module create(String name);

    ModulePath addModule(Module module);

    default boolean hasModule(String name) {
        return getModules().stream().anyMatch(module -> name.equals(module.getName()));
    }

    default int getAmountOfUsedPrototypes() {
        return StreamUtils.sum(getModules(), Module::getAmountOfUsedPrototypes);
    }

    default int getAmountOfReferences() {
        return StreamUtils.sum(getModules(), Module::getAmountOfReferences);
    }

    default Module getDefaultModule() {
        return get(DEFAULT_MODULE).orElseThrow(() -> new PandaParserException(getClass() + " does not have default module"));
    }

    Collection<? extends Module> getModules();

    Optional<Module> get(String name);

}
