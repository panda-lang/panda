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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.panda.framework.design.resource.Language;
import org.panda_lang.panda.framework.language.interpreter.messenger.PandaMessenger;
import org.panda_lang.panda.framework.language.interpreter.messenger.defaults.DefaultOutputListener;
import org.panda_lang.panda.framework.language.interpreter.messenger.translators.InterpreterFailureTranslator;
import org.panda_lang.panda.framework.language.interpreter.messenger.translators.PandaLexerFailureTranslator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class PandaInterpretation implements Interpretation {

    private final Environment environment;
    private final Interpreter interpreter;
    private final Language language;
    private final Messenger messenger;
    private final Collection<InterpreterFailure> failures;

    public PandaInterpretation(Environment environment, Interpreter interpreter, Language language) {
        this.environment = environment;
        this.interpreter = interpreter;
        this.language = language;
        this.failures = new ArrayList<>(1);

        this.messenger = new PandaMessenger();
        this.messenger.addMessageTranslator(new PandaLexerFailureTranslator());
        this.messenger.addMessageTranslator(new InterpreterFailureTranslator(this));
        this.messenger.setOutputListener(new DefaultOutputListener());
    }

    @Override
    public Interpretation execute(Runnable runnable) {
        if (!this.isHealthy()) {
            return this;
        }

        try {
            runnable.run();
        } catch (Exception exception) {
            this.getMessenger().send(exception);
        }

        return this;
    }

    @Override
    public @Nullable <T> T execute(Supplier<T> callback) {
        try {
            return isHealthy() ? callback.get() : null;
        } catch (Exception exception) {
            this.getMessenger().send(exception);
        }

        return null;
    }

    @Override
    public Collection<InterpreterFailure> getFailures() {
        return failures;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
