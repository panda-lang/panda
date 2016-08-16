package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.Application;

public interface ParserInfo {

    ParserContext getParserContext();

    void setParserContext(ParserContext parserContext);

    ParserStatus getParserStatus();

    Application getApplication();
}
