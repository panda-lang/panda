package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.Interpreter;

public interface ParserInfo {

    ParserContext getParserContext();

    void setParserContext(ParserContext parserContext);

    ParserPipeline getParserPipeline();

    Interpreter getInterpreter();

}
