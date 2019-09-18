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

import org.panda_lang.framework.design.architecture.module.LivingModule;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.utilities.commons.iterable.ResourcesIterable;

public class PandaLivingModule extends PandaModule implements LivingModule {

    protected final Module module;
    protected final ModuleLoader loader;

    public PandaLivingModule(ModuleLoader loader, Module module) {
        super(module.getName());

        this.module = module;
        this.loader = loader;
    }

    @Override
    public int getAmountOfReferences() {
        return super.getAmountOfReferences() + module.getAmountOfReferences();
    }

    @Override
    public Iterable<PrototypeReference> getReferences() {
        return new ResourcesIterable<>(super.getReferences(), module.getReferences());
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
    }

    @Override
    public Module getModule() {
        return module;
    }

}
