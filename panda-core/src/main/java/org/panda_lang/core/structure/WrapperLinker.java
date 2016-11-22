package org.panda_lang.core.structure;

public interface WrapperLinker {

    int reserveSlot();

    void linkWrapper(int id, Wrapper wrapper);

    Wrapper getCurrentWrapper();

}
