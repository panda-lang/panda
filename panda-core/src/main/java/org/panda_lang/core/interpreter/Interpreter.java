package org.panda_lang.core.interpreter;

import org.panda_lang.core.Application;

public interface Interpreter {

    Application interpret();

    SourceSet getSourceSet();

}
