package org.panda_lang.panda.utilities.commons.function;

@FunctionalInterface
public interface ThrowingSupplier<T> {

    T get() throws Throwable;

}