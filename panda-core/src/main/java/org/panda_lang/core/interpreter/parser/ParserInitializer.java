package org.panda_lang.core.interpreter.parser;

public interface ParserInitializer<T extends MatchedParser> {

    T createInstance();

}
