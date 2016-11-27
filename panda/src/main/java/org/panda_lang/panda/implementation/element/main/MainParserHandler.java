package org.panda_lang.panda.implementation.element.main;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.interpreter.token.util.TokenUtils;

public class MainParserHandler implements ParserHandler {

    @Override
    public boolean handle(TokenReader tokenReader) {
        TokenRepresentation tokenRepresentation = tokenReader.next();
        return tokenRepresentation != null && TokenUtils.equals(tokenRepresentation, TokenType.KEYWORD, "main");
    }

}
