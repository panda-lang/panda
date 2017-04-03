/*
 * Copyright (c) 2015-2017 Dzikoysk
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

import org.panda_lang.panda.framework.interpreter.source.Source;
import org.panda_lang.panda.framework.interpreter.source.SourceProvider;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.source.PandaSourceSet;
import org.panda_lang.panda.implementation.interpreter.source.util.FileSourceProvider;
import org.panda_lang.panda.implementation.interpreter.source.util.LoaderException;
import org.panda_lang.panda.implementation.structure.PandaApplication;

import java.io.File;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public PandaApplication loadFiles(File... files) {
        return load(new FileSourceProvider(files));
    }

    public PandaApplication load(SourceProvider provider) {
        PandaSourceSet pandaSourceSet = new PandaSourceSet();

        for (Source source : provider) {
            pandaSourceSet.addSource(source);
        }

        if (pandaSourceSet.getSources().isEmpty()) {
            throw new LoaderException("Sources are not provided");
        }

        PandaInterpreter interpreter = new PandaInterpreter(panda, pandaSourceSet);
        interpreter.interpret();

        return interpreter.getApplication();
    }

}
