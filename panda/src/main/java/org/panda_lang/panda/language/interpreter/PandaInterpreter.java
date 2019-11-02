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

package org.panda_lang.panda.language.interpreter;

import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.interpreter.Interpretation;
import org.panda_lang.framework.design.interpreter.Interpreter;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.design.resource.Language;
import org.panda_lang.framework.language.architecture.prototype.generator.PrototypeGeneratorManager;
import org.panda_lang.framework.language.interpreter.PandaInterpretation;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParser;
import org.panda_lang.framework.language.interpreter.pattern.descriptive.extractor.ExtractorWorker;
import org.panda_lang.panda.language.architecture.PandaApplication;
import org.panda_lang.panda.language.interpreter.parser.ApplicationParser;
import org.panda_lang.utilities.commons.TimeUtils;
import org.slf4j.event.Level;

import java.util.Optional;

public final class PandaInterpreter implements Interpreter {

    private final Environment environment;
    private final Language language;

    protected PandaInterpreter(PandaInterpreterBuilder builder) {
        this.environment = builder.environment;
        this.language = builder.elements;
    }

    @Override
    public Optional<Application> interpret(Source source) {
        Interpretation interpretation = new PandaInterpretation(language, environment, this);
        long uptime = System.nanoTime();

        ApplicationParser parser = new ApplicationParser(interpretation);
        PandaApplication application = parser.parse(source);

        if (!interpretation.isHealthy()) {
            environment.getMessenger().send(Level.ERROR, "Interpretation failed, cannot parse specified sources");
            return Optional.empty();
        }

        String parseTime = TimeUtils.toMilliseconds(System.nanoTime() - uptime);

        environment.getMessenger().send(Level.DEBUG, "--- Interpretation of " + source.getId() + " details ");
        environment.getMessenger().send(Level.DEBUG, "• Parse time: " + parseTime);
        environment.getMessenger().send(Level.DEBUG, "• Amount of Prototypes: " + environment.getModulePath().countPrototypes());
        environment.getMessenger().send(Level.DEBUG, "• Amount of used prototypes: " + environment.getModulePath().countUsedPrototypes());
        environment.getMessenger().send(Level.DEBUG, "• Amount of cached references: " + PrototypeGeneratorManager.getInstance().getCacheSize());
        environment.getMessenger().send(Level.DEBUG, "• Expression Parser Time: " + TimeUtils.toMilliseconds(PandaExpressionParser.time) + " (" +  PandaExpressionParser.amount + ")");
        environment.getMessenger().send(Level.DEBUG, "• Pipeline Handle Time: " + TimeUtils.toMilliseconds(environment.getController().getResources().getPipelinePath().getTotalHandleTime()));
        environment.getMessenger().send(Level.DEBUG, "");

        ExtractorWorker.fullTime = 0;
        PandaExpressionParser.time = 0;
        PandaExpressionParser.amount = 0;

        return Optional.of(application);
    }

    public static PandaInterpreterBuilder builder() {
        return new PandaInterpreterBuilder();
    }

    public static final class PandaInterpreterBuilder {

        protected Environment environment;
        protected Language elements;

        private PandaInterpreterBuilder() { }

        public PandaInterpreterBuilder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public PandaInterpreterBuilder elements(Language elements) {
            this.elements = elements;
            return this;
        }

        public PandaInterpreter build() {
            return new PandaInterpreter(this);
        }

    }

}
