package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PandaParserPipeline implements ParserPipeline {

    private final List<ParserRepresentation> representations;
    private final Comparator<ParserRepresentation> comparator;
    private int count;

    public PandaParserPipeline() {
        this.representations = new ArrayList<>();
        this.comparator = new ParserRepresentationComparator();
    }

    @Override
    public UnifiedParser handle(TokenReader tokenReader) {
        if (count > 100) {
            count = 0;
            sort();
        }

        for (ParserRepresentation representation : representations) {
            ParserHandler parserHandler = representation.getHandler();

            if (parserHandler.handle(tokenReader)) {
                representation.increaseUsages();
                count++;

                return representation.getParser();
            }
        }

        return null;
    }

    protected void sort() {
        Collections.sort(representations, comparator);
    }

    @Override
    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        representations.add(parserRepresentation);
        sort();
    }

    public List<ParserRepresentation> getRepresentations() {
        return representations;
    }

}
