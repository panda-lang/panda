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

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.module.Modules;
import org.panda_lang.framework.language.architecture.module.PandaModuleFactory;
import org.panda_lang.framework.language.architecture.prototype.generator.PrototypeGeneratorManager;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.framework.language.resource.internal.PandaFrameworkModules;
import org.panda_lang.panda.language.resource.internal.PandaModules;

import java.util.Optional;

public final class ResourcesLoader {

    public void load(ModuleLoader loader) {
        load(loader, PandaFrameworkModules.getClasses());
        load(loader, PandaModules.getClasses());
    }

    public void load(ModuleLoader loader, Class<? extends InternalModuleInfo>[] internalModuleInfoClasses) {
        PandaModuleFactory moduleFactory = new PandaModuleFactory(loader);

        for (Class<? extends InternalModuleInfo> internalModuleInfoClass : internalModuleInfoClasses) {
            try {
                load(moduleFactory, internalModuleInfoClass.newInstance());
            } catch (Exception e) {
                throw new PandaFrameworkException("Cannot load internal module", e);
            }
        }
    }

    private void load(PandaModuleFactory factory, InternalModuleInfo internalModuleInfo) throws ClassNotFoundException {
        Module module = factory.computeIfAbsent(internalModuleInfo.getModule());

        for (String name : internalModuleInfo.getNames()) {
            Class<?> type = Class.forName(internalModuleInfo.getPackageName() + "." + name);
            module.add(PrototypeGeneratorManager.getInstance().generate(module, type, type.getSimpleName()));
        }

        internalModuleInfo.initialize(module);
    }

}
