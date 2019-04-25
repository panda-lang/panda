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

package org.panda_lang.panda.framework.design.interpreter;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.PandaApplication;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.resource.Language;
import org.panda_lang.panda.framework.language.interpreter.PandaInterpretation;
import org.panda_lang.panda.framework.language.resource.parsers.ApplicationParser;
import org.panda_lang.panda.utilities.commons.TimeUtils;

public class PandaInterpreter implements Interpreter {

    private final Environment environment;
    private final Language language;

    protected PandaInterpreter(PandaInterpreterBuilder builder) {
        this.environment = builder.environment;
        this.language = builder.elements;
    }

    @Override
    public PandaApplication interpret(SourceSet sources) {
        Interpretation interpretation = new PandaInterpretation(environment, this, language);
        long uptime = System.nanoTime();

        ApplicationParser parser = new ApplicationParser(interpretation);
        PandaApplication application = parser.parse(sources);

        if (!interpretation.isHealthy()) {
            interpretation.getMessenger().sendMessage(MessengerLevel.FAILURE, "Interpretation failed, cannot parse specified sources");
            return null;
        }

        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Interpretation details ");
        PandaFramework.getLogger().debug("• Parse time: " + TimeUtils.toMilliseconds(System.nanoTime() - uptime));
        PandaFramework.getLogger().debug("");

        return application;
    }

    public static PandaInterpreterBuilder builder() {
        return new PandaInterpreterBuilder();
    }

}
