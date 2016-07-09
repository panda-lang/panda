package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserRepresentation;
import org.panda_lang.core.work.Executable;

public class PandaParser implements Parser {

    private final Interpreter interpreter;

    public PandaParser(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public Executable parse(ParserInfo parserInfo) {
        return null;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

}
