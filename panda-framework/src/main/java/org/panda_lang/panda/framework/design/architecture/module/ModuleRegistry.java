/*
 * Copyright (c) 2015-2018 Dzikoysk
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
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.utilities.commons.io.PackageUtils;

import java.util.Collection;

public interface ModuleRegistry {

    default int countPrototypes() {
        int count = 0;

        for (Module module : this.getModules()) {
            count += module.getPrototypeCount();
        }

        return count;
    }

    default ClassPrototype forClass(Class<?> clazz) {
        return forName(PackageUtils.toString(clazz.getPackage(), "") + ":" + clazz.getSimpleName());
    }

    ClassPrototype forName(String full);

    Module getOrCreate(@Nullable String groupName);

    Module get(@Nullable String groupName);

    Collection<Module> getModules();

}
