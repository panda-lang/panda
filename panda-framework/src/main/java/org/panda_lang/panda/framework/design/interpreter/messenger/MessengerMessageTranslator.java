package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface MessengerMessageTranslator<T> {

    void handle(Messenger messenger, T element);

    Class<T> getType();

}
