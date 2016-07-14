package org.panda_lang.core.interpreter.parser.representation;

import org.panda_lang.core.interpreter.parser.redact.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ParserRepresentationPipeline {

    private final List<ParserRepresentationHandler> handlers;

    public ParserRepresentationPipeline() {
        handlers = new ArrayList<>();
    }

    public ParserRepresentationHandler handle(Fragment fragment) {
        for (ParserRepresentationHandler handler : handlers) {
            if (handler.handle(fragment)) {
                return handler;
            }
        }
        return null;
    }

    public void add(ParserRepresentationHandler handler) {
        handlers.add(handler);
    }

    public void add(double priority, ParserRepresentationHandler handler) {
        int index = -1;
        double comparedPriority = -1;

        for (int i = 0; i < handlers.size(); i++) {
            ParserRepresentationHandler comparedHandler = handlers.get(i);

            if (comparedHandler.getPriority() > priority) {
                continue;
            }

            if (comparedPriority != -1 && comparedPriority < comparedHandler.getPriority()) {
                continue;
            }

            index = i;
            comparedPriority = comparedHandler.getPriority();
        }

        if (index == -1) {
            handlers.add(handler);
            return;
        }

        handlers.add(index, handler);
    }

    public void addBefore(String handlerName, ParserRepresentationHandler handler) {
        ParserRepresentationHandler searchedHandler = get(handlerName);

        if (searchedHandler == null) {
            add(handler);
        }

        int index = handlers.indexOf(searchedHandler);
        handlers.add(index, handler);
    }

    public ParserRepresentationHandler get(String handlerName) {
        for (ParserRepresentationHandler parserRepresentationHandler : handlers) {
            if (parserRepresentationHandler.getHandlerName().equals(handlerName)) {
                return parserRepresentationHandler;
            }
        }
        return null;
    }

    public List<ParserRepresentationHandler> getHandlers() {
        return handlers;
    }

}
