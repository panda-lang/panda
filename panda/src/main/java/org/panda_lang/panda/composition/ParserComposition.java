package org.panda_lang.panda.composition;

import org.panda_lang.panda.composition.parser.MethodParser;
import org.panda_lang.panda.lang.interpreter.parser.ParserPipeline;
import org.panda_lang.panda.lang.interpreter.parser.ParserRepresentation;

public class ParserComposition {

    private final ParserPipeline pipeline;

    public ParserComposition() {
        this.pipeline = new ParserPipeline();
    }

    protected void initialize() {
        ParserRepresentation methodParserRepresentation = new ParserRepresentation(new MethodParser(), new MethodParser.MethodParserHandler());
        pipeline.registerParserRepresentation(methodParserRepresentation);
    }

    public ParserPipeline getPipeline() {
        return pipeline;
    }

}
