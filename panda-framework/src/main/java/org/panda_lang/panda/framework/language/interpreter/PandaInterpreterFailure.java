package org.panda_lang.panda.framework.language.interpreter;

import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;

public class PandaInterpreterFailure implements InterpreterFailure {

    public PandaInterpreterFailure(String title, ParserData data) {

    }

    @Override
    public String getSourceFile() {
        return null;
    }

    @Override
    public String getLine() {
        return null;
    }

}
