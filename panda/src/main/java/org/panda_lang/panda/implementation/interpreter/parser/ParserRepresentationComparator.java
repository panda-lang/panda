package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.parser.ParserRepresentation;

import java.util.Comparator;

public class ParserRepresentationComparator implements Comparator<ParserRepresentation> {

    @Override
    public int compare(ParserRepresentation parserRepresentation, ParserRepresentation parserRepresentationTo) {
        int priority = Integer.compare(parserRepresentation.getPriority(), parserRepresentationTo.getPriority());

        if (priority != 0) {
            return priority;
        }

        return Integer.compare(parserRepresentation.getUsages(), parserRepresentationTo.getUsages());
    }

}
