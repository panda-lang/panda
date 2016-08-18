package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;

public interface ParserPipeline {

    Parser handle(TokenReader tokenReader);

    void registerParserRepresentation(ParserRepresentation parserRepresentation);

}
