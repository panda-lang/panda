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

package org.panda_lang.panda.framework.language.interpreter;

import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.language.architecture.PandaApplication;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ApplicationParser;
import org.panda_lang.panda.framework.language.resource.PandaLanguage;

public class PandaInterpreter implements Interpreter {

    private final Environment environment;
    private final PandaLanguage language;

    protected PandaInterpreter(PandaInterpreterBuilder builder) {
        this.environment = builder.environment;
        this.language = builder.elements;
    }

    @Override
    public PandaApplication interpret(SourceSet sources) {
        Interpretation interpretation = new PandaInterpretation(environment, this, language);

        ApplicationParser parser = new ApplicationParser(interpretation);
        PandaApplication application = parser.parse(sources);

        if (!interpretation.isHealthy()) {
            interpretation.getMessenger().sendMessage(MessengerLevel.FAILURE, "Interpretation failed, cannot parse specified sources");
            return null;
        }

        return application;
    }

    public static PandaInterpreterBuilder builder() {
        return new PandaInterpreterBuilder();
    }

}
