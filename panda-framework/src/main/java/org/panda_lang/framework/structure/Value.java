package org.panda_lang.framework.structure;

/**
 * Mutable wrapper for java object
 */
public interface Value {

    void setValue(Object value);

    Object getValue();

}
