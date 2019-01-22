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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.PackageUtils;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;

public interface ModulePath {

    Module create(String groupName);

    ModulePath addModule(Module module);

    default Module create(Class<?> clazz) {
        return this.create(PackageUtils.getPackageName(clazz));
    }

    default boolean hasModule(String moduleName) {
        return getModules().stream().anyMatch(module -> moduleName.equals(module.getName()));
    }

    default int getAmountOfUsedPrototypes() {
        return StreamUtils.sum(getModules(), Module::getAmountOfUsedPrototypes);
    }

    default int getAmountOfReferences() {
        return StreamUtils.sum(getModules(), Module::getAmountOfReferences);
    }

    default @Nullable Module get(Class<?> clazz) {
        return this.get(PackageUtils.getPackageName(clazz));
    }

    @Nullable Module get(String groupName);

    Collection<? extends Module> getModules();

}
