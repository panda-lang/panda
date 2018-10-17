package org.panda_lang.panda.utilities.commons.function;

@FunctionalInterface
public interface ThrowingRunnable {

    void run() throws Exception;

}