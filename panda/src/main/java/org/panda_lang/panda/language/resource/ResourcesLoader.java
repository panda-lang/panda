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

package org.panda_lang.panda.language.resource;

import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.language.resource.internal.InternalModuleInfo;
import org.panda_lang.panda.language.resource.internal.PandaModules;

public final class ResourcesLoader {

    public void load(ModulePath path) throws InstantiationException, IllegalAccessException {
        load(path, PandaModules.getClasses());
    }

    public void load(ModulePath path, Class<? extends InternalModuleInfo>[] internalModuleInfoClasses) throws IllegalAccessException, InstantiationException {
        for (int index = 0; index < internalModuleInfoClasses.length; index++) {
            load(path, internalModuleInfoClasses[index].newInstance());
        }
    }

    private void load(ModulePath path, InternalModuleInfo internalModuleInfo) {

    }

}
