/*
 * Copyright (c) 2015-2016 Dzikoysk
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

import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.PandaSourceSet;
import org.panda_lang.panda.implementation.structure.PandaApplication;

import java.io.File;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public PandaApplication loadSingleFile(File file) {
        if (file == null) {
            System.out.println("[PandaLoader] File is null");
            return null;
        }
        else if (!file.exists()) {
            System.out.println("[PandaLoader] File '" + file.getName() + "' doesn't exist.");
            return null;
        }

        PandaSourceSet pandaSourceSet = new PandaSourceSet();
        pandaSourceSet.add(file);

        PandaInterpreter interpreter = new PandaInterpreter(panda, pandaSourceSet);
        interpreter.interpret();

        PandaApplication application = interpreter.getApplication();
        application.setWorkingDirectory(file.getParent());

        return application;
    }

}
