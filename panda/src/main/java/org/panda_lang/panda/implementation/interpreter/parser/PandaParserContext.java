package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.ParserContext;

public class PandaParserContext implements ParserContext {

    private final String source;
    private final TokenizedSource tokenizedSource;
    private TokenReader tokenReader;

    public PandaParserContext(String source, TokenizedSource tokenizedSource) {
        this.source = source;
        this.tokenizedSource = tokenizedSource;
    }

    @Override
    public void setTokenReader(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
    }

    @Override
    public TokenReader getTokenReader() {
        return tokenReader;
    }

    @Override
    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

    @Override
    public String getSource() {
        return source;
    }

}
