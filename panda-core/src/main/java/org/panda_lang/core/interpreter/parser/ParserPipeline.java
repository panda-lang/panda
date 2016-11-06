package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;

public interface ParserPipeline {

    /**
     * @param tokenReader source
     * @return parser which fits to source
     */
    Parser handle(TokenReader tokenReader);

    /**
     * @param parserRepresentation specified parser representation which will be registered in pipeline
     */
    void registerParserRepresentation(ParserRepresentation parserRepresentation);

}
