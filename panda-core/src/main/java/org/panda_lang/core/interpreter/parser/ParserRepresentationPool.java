package org.panda_lang.core.interpreter.parser;

import java.util.Collection;

public class ParserRepresentationPool {

    private Collection<ParserRepresentation> parserRepresentations;

    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        parserRepresentations.add(parserRepresentation);
    }

    public Collection<ParserRepresentation> getParserRepresentations() {
        return parserRepresentations;
    }

}
