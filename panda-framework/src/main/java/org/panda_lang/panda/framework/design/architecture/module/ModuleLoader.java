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

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;

import java.util.Collection;
import java.util.Optional;

public interface ModuleLoader {

    ModuleLoader include(Module module);

    ModuleLoader include(ModulePath path, String name);

    Optional<ClassPrototypeReference> forClass(String name);

    default Optional<ClassPrototypeReference> forClass(Class<?> clazz) {
        return this.forClass(clazz.getSimpleName());
    }

    default LivingModule getDefaultModule() {
        return get(ModulePath.DEFAULT_MODULE).orElseThrow(() -> new PandaParserException(getClass() + " does not have default module"));
    }

    Optional<LivingModule> get(String name);

    Collection<String> names();

    ModulePath getPath();

}
