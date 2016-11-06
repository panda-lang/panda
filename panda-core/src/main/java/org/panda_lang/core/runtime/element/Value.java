package org.panda_lang.core.runtime.element;

/**
 * Mutable wrapper for java object
 */
public interface Value extends Executable {

    /**
     * @param parameters parameters for executable process
     * @return itself
     */
    @Override
    default Value execute(Value... parameters) {
        return this;
    }

    void setValue(Object value);

    Object getValue();

}
