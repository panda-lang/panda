package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Statement;

import java.util.Iterator;

public class OverallParser implements Iterator<Statement>, Iterable<Statement> {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final TokenReader tokenReader;

    public OverallParser(ParserInfo parserInfo, TokenReader tokenReader) {
        this.parserInfo = parserInfo;
        this.pipeline = parserInfo.getComponent(Components.PARSER_PIPELINE);
        this.tokenReader = tokenReader;
    }

    @Override
    public Statement next() {
        ParserInfo modifiedParserInfo = parserInfo.clone();
        modifiedParserInfo.setComponent(Components.READER, tokenReader);

        while (tokenReader.hasNext()) {
            tokenReader.synchronize();

            UnifiedParser parser = pipeline.handle(tokenReader);

            if (parser == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Unrecognized " + tokenReader.read());
            }

            Statement statement = parser.parse(modifiedParserInfo);

            if (statement == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Failed to parse statement at line " + (tokenReader.previous().getLine() + 1));
            }

            return statement;
        }

        return null;
    }

    @Override
    public Iterator<Statement> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return tokenReader.hasNext();
    }

}
