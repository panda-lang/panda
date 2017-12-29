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

package org.panda_lang.panda.core.interpreter;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.interpreter.parser.defaults.SourceParser;
import org.panda_lang.panda.core.structure.PandaApplication;
import org.panda_lang.panda.framework.language.interpreter.Interpreter;
import org.panda_lang.panda.framework.language.interpreter.source.SourceSet;

public class PandaInterpreter implements Interpreter {

    private final Panda panda;
    private final SourceSet sourceSet;
    private final PandaApplication application;

    public PandaInterpreter(Panda panda, SourceSet sourceSet) {
        this.panda = panda;
        this.sourceSet = sourceSet;
        this.application = new PandaApplication();
    }

    @Override
    public void interpret() {
        SourceParser parser = new SourceParser(this);
        parser.parse(sourceSet);
    }

    public PandaApplication getApplication() {
        return application;
    }

    public Panda getPanda() {
        return panda;
    }

}
