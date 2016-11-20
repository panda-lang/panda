package org.panda_lang.core.interpreter;

import org.panda_lang.core.structure.Application;

public interface Interpreter {

    /**
     * Starts the process of interpretation
     */
    void interpret();

    /**
     * @return an application that may not be ready yet
     */
    Application getApplication();

}