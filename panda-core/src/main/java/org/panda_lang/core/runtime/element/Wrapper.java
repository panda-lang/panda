package org.panda_lang.core.runtime.element;

/**
 * Specific type of scope which contains own memory, independence, etc.
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
