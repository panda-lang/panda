package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.match.hollow.HollowPattern;

import java.util.ArrayList;
import java.util.Collection;

public class ParserRepresentation {

    private final Parser parser;
    private final Collection<HollowPattern> patterns;

    public ParserRepresentation(Parser parser) {
        this.parser = parser;
        this.patterns = new ArrayList<>();
    }

    public void registerPattern(HollowPattern hollowPattern) {
        patterns.add(hollowPattern);
    }

    public Collection<HollowPattern> getPatterns() {
        return patterns;
    }

    public Parser getParser() {
        return parser;
    }

}
