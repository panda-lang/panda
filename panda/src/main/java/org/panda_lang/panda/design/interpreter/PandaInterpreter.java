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

package org.panda_lang.panda.design.interpreter;

import org.panda_lang.panda.design.architecture.PandaApplication;
import org.panda_lang.panda.design.interpreter.parser.defaults.ApplicationParser;
import org.panda_lang.panda.language.elements.PandaElements;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;

public class PandaInterpreter implements Interpreter {

    private final Environment environment;
    private final PandaElements pandaElements;

    protected PandaInterpreter(PandaInterpreterBuilder builder) {
        this.environment = builder.environment;
        this.pandaElements = builder.elements;
    }

    @Override
    public PandaApplication interpret(SourceSet sources) {
        ApplicationParser parser = new ApplicationParser(this);
        return parser.parse(sources);
    }

    public PandaElements getPandaElements() {
        return pandaElements;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public static PandaInterpreterBuilder builder() {
        return new PandaInterpreterBuilder();
    }

}
