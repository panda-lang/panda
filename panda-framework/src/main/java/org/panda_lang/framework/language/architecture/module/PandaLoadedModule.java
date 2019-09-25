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

package org.panda_lang.framework.language.architecture.module;

import org.panda_lang.framework.design.architecture.module.LoadedModule;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayClassPrototypeFetcher;
import org.panda_lang.framework.language.architecture.prototype.array.PandaArray;

import java.util.Optional;

public final class PandaLoadedModule implements LoadedModule {

    private final ModuleLoader loader;
    private final Module module;

    public PandaLoadedModule(ModuleLoader loader, Module module) {
        this.loader = loader;
        this.module = module;
    }

    @Override
    public Optional<PrototypeReference> forClass(Class<?> associatedClass) {
        if (associatedClass.isArray()) {
            ArrayClassPrototypeFetcher.fetch(module, associatedClass);
        }

        return module.forClass(associatedClass);
    }

    @Override
    public Optional<PrototypeReference> forName(CharSequence prototypeName) {
        String name = prototypeName.toString();

        if (name.endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassPrototypeFetcher.fetch(module, name);
        }

        return module.forName(name);
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
    }

}
