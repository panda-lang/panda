package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.Application;

public interface ParserInfo {

    void setParserContext(ParserContext parserContext);

    ParserContext getParserContext();

    ParserStatus getParserStatus();

    Application getApplication();
}
