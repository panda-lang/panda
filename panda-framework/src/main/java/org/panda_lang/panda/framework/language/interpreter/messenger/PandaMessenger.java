package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;
import org.panda_lang.panda.utilities.commons.collection.*;

import java.util.*;

public class PandaMessenger implements Messenger {

    private final List<MessengerMessageTranslator> translators = new ArrayList<>();
    private MessengerOutputListener outputListener;

    @Override
    @SuppressWarnings("unchecked")
    public void send(Object message) {
        MessengerMessageTranslator translator = null;

        for (MessengerMessageTranslator messageTranslator : new ReverseIterator<>(translators)) {
            if (messageTranslator.getType().isAssignableFrom(message.getClass())) {
                translator = messageTranslator;
                break;
            }
        }

        if (translator == null) {
            throw new PandaFrameworkException("Cannot translate a message - translator for " + message.getClass() + " not found");
        }

        translator.handle(this, message);
    }

    @Override
    public void sendMessage(MessengerMessage.Level level, String message) {
        MessengerMessage generatedMessage = new PandaMessengerMessage(level, message);
        sendMessage(generatedMessage);
    }

    @Override
    public void sendMessage(MessengerMessage message) {
        outputListener.onMessage(message);
    }

    @Override
    public void addMessageTranslator(MessengerMessageTranslator translator) {
        translators.add(translator);
    }

    @Override
    public void setOutputListener(MessengerOutputListener listener) {
        this.outputListener = listener;
    }

}
