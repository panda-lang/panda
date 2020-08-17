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
import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.interpreter.Interpretation;
import org.panda_lang.language.interpreter.Interpreter;
import org.panda_lang.language.interpreter.PandaInterpretation;
import org.panda_lang.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.language.interpreter.source.Source;
import org.panda_lang.panda.PandaException;
import org.panda_lang.panda.language.architecture.PandaApplication;
import org.panda_lang.panda.language.interpreter.parser.ApplicationParser;
import org.panda_lang.utilities.commons.TimeUtils;
import org.panda_lang.utilities.commons.function.ThrowingConsumer;

public final class PandaInterpreter implements Interpreter {

    private final Environment environment;

    public PandaInterpreter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Application interpret(Source source) {
        return interpret(source, interpretation -> {});
    }

    @Override
    public Application interpret(Source source, @Nullable ThrowingConsumer<Interpretation, ?> interpretationConsumer) {
        Interpretation interpretation = new PandaInterpretation(this);
        ApplicationParser parser = new ApplicationParser(interpretation);

        if (interpretationConsumer != null) {
            interpretation.execute(() -> {
                interpretationConsumer.accept(interpretation);
            });
        }

        long uptime = System.nanoTime();
        PandaApplication application = parser.parse(source);

        if (!interpretation.isHealthy()) {
            throw new PandaException("Interpretation failed, cannot parse specified sources");
        }

        String parseTime = TimeUtils.toMilliseconds(System.nanoTime() - uptime);

        environment.getLogger().debug("--- Interpretation of " + source.getId() + " details ");
        environment.getLogger().debug("• Parse time: " + parseTime);
        environment.getLogger().debug("• Amount of types: " + environment.getModulePath().countTypes());
        environment.getLogger().debug("• Amount of used types: " + environment.getModulePath().countUsedTypes());
        // environment.getLogger().debG, "• Amount of cached references: " + TypeGeneratorManager.getInstance().getCacheSize());
        environment.getLogger().debug("• Expression Parser Time: " + TimeUtils.toMilliseconds(PandaExpressionParser.time) + " (" + PandaExpressionParser.amount + ")");
        environment.getLogger().debug("• Pipeline Handle Time: " + TimeUtils.toMilliseconds(environment.getController().getResources().getPipelinePath().getTotalHandleTime()));
        environment.getLogger().debug("");

        PandaExpressionParser.time = 0;
        PandaExpressionParser.amount = 0;

        return application;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
