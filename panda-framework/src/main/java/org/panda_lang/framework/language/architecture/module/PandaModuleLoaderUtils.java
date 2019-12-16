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

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.module.Modules;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Optional;

final class PandaModuleLoaderUtils {

    private PandaModuleLoaderUtils() { }

    protected static Optional<Module> fetch(ModuleLoader loader, Modules modules, String moduleQualifier, boolean compute) {
        String[] names = moduleQualifier.split(":");
        Module module = null;

        for (String name : names) {
            if (StringUtils.isEmpty(name)) {
                throw new PandaFrameworkException("Illegal name " + moduleQualifier);
            }

            Module nextModule = modules.get(name, loader).orElse(null);

            if (nextModule == null && compute) {
                nextModule = new PandaModule(module, name, loader);
                modules.include(nextModule);
            }

            if (nextModule == null) {
                return Optional.empty();
            }

            module = nextModule;
            modules = module;
        }

        if (module != null) {
            loader.loadIfAbsent(module);
        }

        return Optional.ofNullable(module);
    }

}
