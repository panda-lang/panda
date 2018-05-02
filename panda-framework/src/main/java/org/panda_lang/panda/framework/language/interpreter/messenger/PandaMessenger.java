package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;

import java.util.*;

public class PandaMessenger implements Messenger {

    private final Map<Class<?>, MessengerMessageTranslator> translators = new HashMap<>();
    private final Collection<MessengerOutputListener> outputListeners = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void send(Object message) {
        MessengerMessageTranslator translator = translators.get(message.getClass());

        if (translator == null) {
            throw new PandaFrameworkException("Cannot translate a message - translator for " + message.getClass() + " not found");
        }

        MessengerMessage translatedMessage = translator.translate(message);
        sendMessage(translatedMessage);
    }

    @Override
    public void sendMessage(MessengerMessage.Level level, String message) {
        MessengerMessage generatedMessage = new PandaMessengerMessage(level, message);
        sendMessage(generatedMessage);
    }

    @Override
    public void sendMessage(MessengerMessage message) {
        for (MessengerOutputListener outputListener : outputListeners) {
            outputListener.onMessage(message);
        }
    }

    @Override
    public void addMessageTranslator(MessengerMessageTranslator translator) {
        translators.put(translator.getType(), translator);
    }

    @Override
    public void addOutputListener(MessengerOutputListener listener) {
        outputListeners.add(listener);
    }

}
