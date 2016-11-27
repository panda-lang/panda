package org.panda_lang.framework.structure;

public interface WrapperInstance extends Executable {

    /**
     * @return array of variables which index is equals to order of fields
     */
    Object[] getVariables();

    /**
     * @return proper wrapper
     */
    Wrapper getWrapper();

}
