package org.panda_lang.panda.framework.language.interpreter;

import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.listeners.*;

import java.util.*;

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
        this.messenger.addMessageTranslator(new InterpreterFailureTranslator(this));
        this.messenger.setOutputListener(new DefaultOutputListener());
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
