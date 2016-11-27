package org.panda_lang.framework.interpreter.parser;

public interface ParserInitializer<T extends UnifiedParser> {

    T createInstance();

}
