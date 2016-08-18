package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.Interpreter;

public interface ParserInfo {

    void setParserContext(ParserContext parserContext);

    ParserContext getParserContext();

    ParserPipeline getParserPipeline();

    Interpreter getInterpreter();

}
