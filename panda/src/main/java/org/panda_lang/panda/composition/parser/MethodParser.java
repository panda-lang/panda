package org.panda_lang.panda.composition.parser;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenType;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.work.Executable;

public class MethodParser implements Parser {

    @Override
    public Executable parse(ParserInfo parserInfo) {
        ParserContext parserContext = parserInfo.getParserContext();
        TokenReader tokenReader = parserContext.getTokenReader();

        // TODO: token-oriented pattern
        tokenReader.read();

        return null;
    }

    public static class MethodParserHandler implements ParserHandler {

        @Override
        public boolean handle(TokenReader tokenReader) {
            Token token = tokenReader.next();
            return !(token == null || token.getType() != TokenType.KEYWORD || !token.getToken().equals("method"));
        }

    }

}
