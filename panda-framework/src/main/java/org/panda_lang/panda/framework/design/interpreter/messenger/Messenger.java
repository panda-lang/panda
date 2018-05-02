package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface Messenger {

    void send(Object message);

    void sendMessage(MessengerMessage.Level level, String message);

    void sendMessage(MessengerMessage message);

    void addMessageTranslator(MessengerMessageTranslator translator);

    void addOutputListener(MessengerOutputListener listener);

}
