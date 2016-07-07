package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.parser.match.hollow.HollowPattern;

import java.util.ArrayList;
import java.util.Collection;

public class ParserRepresentation<T extends Parser> {

    private final T parser;
    private final ParserInitializer<T> parserParserInitializer;
    private final Collection<HollowPattern> patterns;

    public ParserRepresentation(T parser, ParserInitializer<T> parserParserInitializer) {
        this.parser = parser;
        this.parserParserInitializer = parserParserInitializer;
        this.patterns = new ArrayList<>();
    }

    public void registerPattern(HollowPattern hollowPattern) {
        patterns.add(hollowPattern);
    }

    public Collection<HollowPattern> getPatterns() {
        return patterns;
    }

    public ParserInitializer<T> getParserParserInitializer() {
        return parserParserInitializer;
    }

    public T getParser() {
        return parser;
    }

}
