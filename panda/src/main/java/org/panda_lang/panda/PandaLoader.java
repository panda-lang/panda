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

package org.panda_lang.panda;

import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.framework.design.architecture.PandaEnvironment;
import org.panda_lang.panda.framework.design.interpreter.PandaInterpreter;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.language.interpreter.source.PandaURLSource;

import java.io.File;
import java.util.Optional;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public Optional<Application> load(String main, File workingDirectory) {
        return load(PandaURLSource.fromFile(new File(workingDirectory, main)), workingDirectory);
    }

    public Optional<Application> load(File main, File workingDirectory) {
        return load(PandaURLSource.fromFile(main), workingDirectory);
    }

    public Optional<Application> load(Source main, File workingDirectory) {
        PandaEnvironment environment = new PandaEnvironment(panda, workingDirectory);
        environment.initialize();

        PandaInterpreter interpreter = environment.getInterpreter();
        return interpreter.interpret(main);
    }

}
