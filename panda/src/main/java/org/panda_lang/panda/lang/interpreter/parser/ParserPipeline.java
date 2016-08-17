package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParserPipeline {

    private final List<ParserRepresentation> representations;
    private final Comparator<ParserRepresentation> comparator;
    private int count;

    public ParserPipeline() {
        this.representations = new ArrayList<>();
        this.comparator = new Comparator<ParserRepresentation>() {
            @Override
            public int compare(ParserRepresentation parserRepresentation, ParserRepresentation parserRepresentationTo) {
                return Integer.compare(parserRepresentation.getUsages(), parserRepresentationTo.getUsages());
            }
        };
    }

    public Parser handle(TokenReader tokenReader) {
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

    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        this.representations.add(parserRepresentation);
    }

    public List<ParserRepresentation> getRepresentations() {
        return representations;
    }

}
