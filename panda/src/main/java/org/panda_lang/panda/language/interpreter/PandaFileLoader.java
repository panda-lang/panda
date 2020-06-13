/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.language.interpreter.source.PandaURLSource;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.language.architecture.PandaEnvironment;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;

public final class PandaFileLoader {

    private final Panda panda;

    public PandaFileLoader(Panda panda) {
        this.panda = panda;
    }

    public Option<Application> load(String script, @Nullable File workingDirectory) {
        return load(PandaURLSource.fromFile(new File(workingDirectory, script)), workingDirectory);
    }

    public Option<Application> load(File script, @Nullable File workingDirectory) {
        if (workingDirectory == null) {
            workingDirectory = script.getParentFile();
        }

        return load(PandaURLSource.fromFile(script), workingDirectory);
    }

    public Option<Application> load(Source script, File workingDirectory) {
        PandaEnvironment environment = new PandaEnvironment(panda, workingDirectory);
        environment.initialize();

        return environment.getInterpreter().interpret(script);
    }

}
