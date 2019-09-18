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

package org.panda_lang.panda.language.architecture;

import org.panda_lang.panda.Panda;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.language.interpreter.PandaInterpreter;
import org.panda_lang.framework.design.resource.Resources;
import org.panda_lang.framework.language.architecture.module.PandaModulePath;
import org.panda_lang.framework.language.resource.PandaTypes;

import java.io.File;

public class PandaEnvironment implements Environment {

    private final Panda panda;
    private final File workingDirectory;
    private ModulePath modulePath;
    private PandaInterpreter interpreter;

    public PandaEnvironment(Panda panda, File workingDirectory) {
        this.panda = panda;
        this.workingDirectory = workingDirectory;
    }

    public void initialize() {
        this.modulePath = new PandaModulePath();

        PandaTypes types = new PandaTypes();
        types.fill(modulePath);

        this.interpreter = PandaInterpreter.builder()
                .environment(this)
                .elements(panda.getLanguage())
                .build();
    }

    @Override
    public Resources getResources() {
        return panda.getResources();
    }

    @Override
    public ModulePath getModulePath() {
        return modulePath;
    }

    @Override
    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

    @Override
    public File getDirectory() {
        return workingDirectory;
    }

}
