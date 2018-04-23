package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.messenger.*;

import java.util.*;

public class PandaMessenger implements Messenger {

    private final Collection<MessengerOutputListener> outputListeners = new ArrayList<>();

    @Override
    public void sendMessage(MessengerMessage message) {
        for (MessengerOutputListener outputListener : outputListeners) {
            outputListener.onMessage(message);
        }
    }

    @Override
    public void addOutputListener(MessengerOutputListener listener) {
        outputListeners.add(listener);
    }

}
