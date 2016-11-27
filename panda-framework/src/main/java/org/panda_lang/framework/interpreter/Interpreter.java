package org.panda_lang.framework.interpreter;

import org.panda_lang.framework.structure.Application;

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