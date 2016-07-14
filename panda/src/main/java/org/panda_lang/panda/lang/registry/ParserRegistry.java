package org.panda_lang.panda.lang.registry;

import org.panda_lang.core.interpreter.parser.representation.ParserRepresentationPipeline;

public class ParserRegistry {

    public static ParserRepresentationPipeline getPipeline() {
        ParserRepresentationPipeline pipeline = new ParserRepresentationPipeline();

        return pipeline;
    }

}
