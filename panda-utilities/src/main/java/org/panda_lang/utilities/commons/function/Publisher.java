package org.panda_lang.utilities.commons.function;

public interface Publisher<T> {

    void subscribe(Subscriber<? super T> subscriber);

}
