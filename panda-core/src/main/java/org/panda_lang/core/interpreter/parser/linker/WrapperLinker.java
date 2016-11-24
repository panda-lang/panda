package org.panda_lang.core.interpreter.parser.linker;

import org.panda_lang.core.structure.Wrapper;

public interface WrapperLinker {

    void pushWrapper(Wrapper wrapper);

    Wrapper popWrapper();

    Wrapper getCurrentWrapper();

    int getNextID();

}
