package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface MessengerMessage {

    String getTitle();

    String getMessageContent();

    MessengerMessage.Level getLevel();

    enum Level {

        INFO,
        WARNING,
        ERROR,
        OTHER

    }

}
