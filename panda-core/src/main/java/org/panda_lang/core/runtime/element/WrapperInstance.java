package org.panda_lang.core.runtime.element;

import org.panda_lang.core.runtime.structure.ExecutableBranch;

public interface WrapperInstance {

    /**
     * @param executableBranch associated branch with instance
     * @return result
     */
    Value execute(ExecutableBranch executableBranch, Value... parameters);

    /**
     * @return array of pointers associated by index to fields
     */
    long[] getPointers();

    /**
     * @return proper wrapper
     */
    Wrapper getWrapper();

}
