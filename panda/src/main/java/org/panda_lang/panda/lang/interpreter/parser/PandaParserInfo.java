package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserPipeline;

public class PandaParserInfo implements ParserInfo {

    private final Interpreter interpreter;
    private final ParserPipeline pipeline;
    private ParserContext parserContext;

    public PandaParserInfo(Interpreter interpreter, ParserPipeline pipeline) {
        this.interpreter = interpreter;
        this.pipeline = pipeline;
    }

    @Override
    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public ParserContext getParserContext() {
        return parserContext;
    }

    @Override
    public ParserPipeline getParserPipeline() {
        return pipeline;
    }

    @Override
    public Interpreter getInterpreter() {
        return interpreter;
    }

}
