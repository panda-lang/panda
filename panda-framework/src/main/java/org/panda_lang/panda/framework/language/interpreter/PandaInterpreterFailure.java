package org.panda_lang.panda.framework.language.interpreter;

import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;

public class PandaInterpreterFailure implements InterpreterFailure {

    private final int line;
    private final ParserData data;
    private final String location;
    private final String message;
    private final String details;

    public PandaInterpreterFailure(String message, String details, ParserData data) {
        this.message = message;
        this.details = details;
        this.line = data.getComponent(UniversalComponents.SOURCE_STREAM).getCurrentLine();
        this.location = data.getComponent(UniversalComponents.SCRIPT).getScriptName();
        this.data = data;
    }

    public PandaInterpreterFailure(String message, ParserData data) {
        this(message, null, data);
    }

    @Override
    public ParserData getData() {
        return data;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public int getLine() {
        return line;
    }

}
