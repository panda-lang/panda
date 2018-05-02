package org.panda_lang.panda.framework.design.interpreter;

import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.framework.language.*;

import java.util.*;

public interface Interpretation {

    default boolean isHealthy() {
        return getFailures().size() == 0;
    }

    Collection<InterpreterFailure> getFailures();

    Messenger getMessenger();

    Language getLanguage();

    Interpreter getInterpreter();

    Environment getEnvironment();

}
