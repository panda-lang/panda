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

package org.panda_lang.panda.framework.language.architecture;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.architecture.module.PrimitivePrototypeLiquid;
import org.panda_lang.panda.framework.language.architecture.prototype.registry.ClassPrototypeModelLoader;
import org.panda_lang.panda.framework.language.interpreter.PandaInterpreter;

public class PandaEnvironment implements Environment {

    protected final Panda panda;
    protected final ModulePath modulePath;
    protected PandaInterpreter interpreter;

    public PandaEnvironment(Panda panda) {
        this.panda = panda;
        this.modulePath = new PandaModulePath();
    }

    public void initialize() {
        PrimitivePrototypeLiquid liquid = new PrimitivePrototypeLiquid();
        liquid.fill(modulePath);

        ClassPrototypeModelLoader modelLoader = new ClassPrototypeModelLoader(modulePath);
        panda.getPandaLanguage().getMappings().forEach((modelLoader::load));

        this.interpreter = PandaInterpreter.builder()
                .environment(this)
                .elements(panda.getPandaLanguage())
                .build();
    }

    public ModulePath getModulePath() {
        return modulePath;
    }

    @Override
    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
