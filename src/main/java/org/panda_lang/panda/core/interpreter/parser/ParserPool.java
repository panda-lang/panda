package org.panda_lang.panda.core.interpreter.parser;

import java.util.Collection;

public class ParserPool {

    private Collection<ParserRepresentation> parserRepresentations;

    public void registerParserRepresentation(ParserRepresentation parserRepresentation) {
        parserRepresentations.add(parserRepresentation);
    }

    public Collection<ParserRepresentation> getParserRepresentations() {
        return parserRepresentations;
    }

}
