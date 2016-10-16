package org.panda_lang.core.work.element;

import org.panda_lang.core.work.Value;

/**
 * Specific type of scope which
 */
public interface Wrapper extends Scope {

    @Override
    default Value execute(Value... parameters) {
        throw new RuntimeException("Wrapper shouldn't be executed without instance");
    }

    /**
     * Creates new instance of the current wrapper for individual values for fields, etc.
     *
     * @return instance of the current wrapper
     */
    WrapperInstance createInstance();

}
