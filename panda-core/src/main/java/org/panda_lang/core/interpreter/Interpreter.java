package org.panda_lang.core.interpreter;

import org.panda_lang.core.Application;

public interface Interpreter {

    void interpret();

    Application getApplication();

}