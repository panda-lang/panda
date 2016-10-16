package org.panda_lang.core.work.element;

public interface WrapperInstance extends Executable {

    /**
     * @return array of pointers
     */
    int[] getPointers();

    /**
     * @return proper wrapper
     */
    Wrapper getWrapper();

}
