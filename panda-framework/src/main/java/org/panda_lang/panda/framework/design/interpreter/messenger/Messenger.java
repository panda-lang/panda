package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface Messenger {

    void sendMessage(MessengerMessage message);

    void addOutputListener(MessengerOutputListener listener);

}
