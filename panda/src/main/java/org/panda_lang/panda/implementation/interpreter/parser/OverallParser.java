package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserPipeline;
import org.panda_lang.core.interpreter.parser.UnifiedParser;
import org.panda_lang.core.interpreter.parser.util.Components;
import org.panda_lang.core.structure.Statement;

import java.util.Iterator;

public class OverallParser implements Iterator<Statement>, Iterable<Statement> {

    private final ParserInfo parserInfo;
    private final ParserPipeline pipeline;
    private final ParserContext parserContext;
    private final TokenReader tokenReader;

    public OverallParser(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;
        this.pipeline = parserInfo.getComponent(Components.PARSER_PIPELINE);
        this.parserContext = parserInfo.getComponent(Components.PARSER_CONTEXT);
        this.tokenReader = parserContext.getTokenReader();
    }

    @Override
    public Statement next() {
        while (tokenReader.hasNext()) {
            tokenReader.synchronize();

            UnifiedParser parser = pipeline.handle(tokenReader);

            if (parser == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Unrecognized " + tokenReader.read());
            }

            Statement statement = parser.parse(parserInfo);

            if (statement == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Failed to parse statement at " + tokenReader.previous().getLine());
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
