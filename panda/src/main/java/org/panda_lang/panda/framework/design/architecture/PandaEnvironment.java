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

package org.panda_lang.panda.framework.design.architecture;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.interpreter.PandaInterpreter;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.resource.prototypes.model.loader.AnnotatedModelsLoader;
import org.panda_lang.panda.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.loader.AutoloadLoader;
import org.panda_lang.panda.util.PandaUtils;

public class PandaEnvironment implements Environment {

    protected final Panda panda;
    protected final ModulePath modulePath;
    protected PandaInterpreter interpreter;

    public PandaEnvironment(Panda panda) {
        this.panda = panda;
        this.modulePath = new PandaModulePath();
    }

    public void initialize() {
        PandaTypes types = new PandaTypes();
        types.fill(modulePath);

        AutoloadLoader autoloadLoader = new AutoloadLoader();
        autoloadLoader.load(PandaUtils.DEFAULT_PANDA_SCANNER);

        AnnotatedModelsLoader modelLoader = new AnnotatedModelsLoader();
        modelLoader.load(modulePath, PandaUtils.DEFAULT_PANDA_SCANNER);

        this.interpreter = PandaInterpreter.builder()
                .environment(this)
                .elements(panda.getPandaLanguage())
                .build();
    }

    @Override
    public PipelinePath getPipelinePath() {
        return panda.getResources().getPipelinePath();
    }

    @Override
    public ModulePath getModulePath() {
        return modulePath;
    }

    @Override
    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
