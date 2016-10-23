package org.panda_lang.core.work.element;

public interface WrapperInstance extends Executable {

    /**
     * @return array of pointers associated by index to fields
     */
    int[] getPointers();

    /**
     * @return proper wrapper
     */
    Wrapper getWrapper();

}
