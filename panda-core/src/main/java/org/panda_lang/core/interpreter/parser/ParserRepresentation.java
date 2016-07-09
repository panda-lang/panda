package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.Registrable;
import org.panda_lang.core.interpreter.parser.match.hollow.HollowPattern;

import java.util.ArrayList;
import java.util.Collection;

public class ParserRepresentation<T extends Parser & Registrable> {

    private final T parser;
    private final Collection<HollowPattern> patterns;

    public ParserRepresentation(T parser) {
        this.parser = parser;
        this.patterns = new ArrayList<>();
    }

    public void registerPattern(HollowPattern hollowPattern) {
        patterns.add(hollowPattern);
    }

    public Collection<HollowPattern> getPatterns() {
        return patterns;
    }

    public T getParser() {
        return parser;
    }

}
