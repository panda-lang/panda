package org.panda_lang.core.interpreter.parser.util;

public interface ArrayDistributor<T> {

    T previous();

    T current();

    boolean hasNext();

    T next();

    T further();

    T future();

    T getPrevious();

    T getPrevious(int i);

    T getLast();

}
