package org.panda_lang.framework.interpreter.parser;

import org.panda_lang.framework.interpreter.lexer.TokenReader;

public interface ParserPipeline {

    /**
     * @param tokenReader source
     * @return parser which fits to source
     */
    UnifiedParser handle(TokenReader tokenReader);

    /**
     * @param parserRepresentation specified parser representation which will be registered in pipeline
     */
    void registerParserRepresentation(ParserRepresentation parserRepresentation);

}
