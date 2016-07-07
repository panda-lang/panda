package org.panda_lang.core.interpreter.parser;

public interface ParserInitializer<T extends Parser> {

    T createInstance();

}
