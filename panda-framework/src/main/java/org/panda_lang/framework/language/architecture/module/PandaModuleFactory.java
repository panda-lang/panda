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
import org.panda_lang.framework.design.architecture.module.ModulePath;

import java.util.function.Supplier;

public final class PandaModuleFactory {

    private final ModulePath path;

    public PandaModuleFactory(ModulePath path) {
        this.path = path;
    }

    public Module computeIfAbsent(String moduleQualifier) {
        return PandaModulesUtils.fetch(path, moduleQualifier, true).orElseThrow((Supplier<? extends PandaFrameworkException>) () -> {
            throw new PandaFrameworkException("Cannot fetch module " + moduleQualifier);
        });
    }

    public ModulePath getPath() {
        return path;
    }

}
