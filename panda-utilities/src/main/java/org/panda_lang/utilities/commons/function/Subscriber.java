package org.panda_lang.utilities.commons.function;

@FunctionalInterface
public interface Subscriber<T> {

    void onComplete(T value);

}
