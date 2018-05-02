package org.panda_lang.panda.framework.design.interpreter;

public interface InterpreterFailure {

    String getDetails();

    String getMessage();

    String getLocation();

    int getLine();

}
