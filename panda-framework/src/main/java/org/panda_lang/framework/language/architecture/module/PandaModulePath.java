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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModulePath;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PandaModulePath extends PandaModules implements ModulePath {

    private final Map<String, Runnable> modules = new HashMap<>();

    public PandaModulePath(Module... modules) {
        for (Module module : modules) {
            include(module);
        }
    }

    @Override
    public void include(String name, Runnable initialize) {
        Runnable cachedInitialize = modules.get(name);

        if (cachedInitialize != null) {
            Runnable nextInitialize = initialize;

            initialize = () -> {
                cachedInitialize.run();
                nextInitialize.run();
            };
        }

        modules.put(name, initialize);
    }

    @Override
    public Optional<Module> get(String moduleQualifier) {
        Runnable initialize = modules.get(moduleQualifier);

        if (initialize != null) {
            modules.remove(moduleQualifier);
            initialize.run();
        }

        return super.get(moduleQualifier);
    }

}
