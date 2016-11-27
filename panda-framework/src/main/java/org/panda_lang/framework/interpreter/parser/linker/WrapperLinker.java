package org.panda_lang.framework.interpreter.parser.linker;

import org.panda_lang.framework.structure.Wrapper;

public interface WrapperLinker {

    void pushWrapper(Wrapper wrapper);

    Wrapper popWrapper();

    Wrapper getCurrentWrapper();

    int getNextID();

}
