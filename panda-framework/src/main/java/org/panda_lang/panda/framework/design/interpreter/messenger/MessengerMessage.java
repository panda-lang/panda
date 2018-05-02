package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface MessengerMessage {

    enum Level {

        INFO,
        WARNING,
        ERROR,
        OTHER

    }

    String getMessage();

    String getDetails();

    MessengerMessage.Level getLevel();

}
