package org.panda_lang.panda.framework.design.interpreter;

import org.jetbrains.annotations.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;

public interface InterpreterFailure {

    @Nullable
    String getDetails();

    ParserData getData();

    String getLocation();

    String getMessage();

    int getLine();

}
