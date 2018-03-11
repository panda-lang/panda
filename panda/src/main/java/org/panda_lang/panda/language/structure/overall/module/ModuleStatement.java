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

package org.panda_lang.panda.language.structure.overall.module;

import org.panda_lang.panda.framework.design.architecture.prototype.module.Module;
import org.panda_lang.panda.framework.design.architecture.Statement;

public class ModuleStatement implements Statement {

    private final Module module;

    public ModuleStatement(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    @Override
    public String toString() {
        return "'module': '" + module.getName() + "'";
    }

}
