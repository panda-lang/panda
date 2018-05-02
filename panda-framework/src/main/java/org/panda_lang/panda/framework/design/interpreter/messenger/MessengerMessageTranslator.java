package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface MessengerMessageTranslator<T> {

    MessengerMessage translate(T element);

    Class<T> getType();

}
