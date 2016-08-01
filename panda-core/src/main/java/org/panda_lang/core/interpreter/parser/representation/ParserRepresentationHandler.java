package org.panda_lang.core.interpreter.parser.representation;

import org.panda_lang.core.interpreter.parser.redact.Fragment;

public class ParserRepresentationHandler {

    private final String handlerName;
    private final ParserRepresentation parserRepresentation;
    private final double priority;

    public ParserRepresentationHandler(String handlerName, double priority, ParserRepresentation parserRepresentation) {
        this.handlerName = handlerName;
        this.parserRepresentation = parserRepresentation;
        this.priority = priority;
    }

    public boolean handle(Fragment fragment) {
        return parserRepresentation.check(fragment);
    }

    public ParserRepresentation getParserRepresentation() {
        return parserRepresentation;
    }

    public double getPriority() {
        return priority;
    }

    public String getHandlerName() {
        return handlerName;
    }

}
