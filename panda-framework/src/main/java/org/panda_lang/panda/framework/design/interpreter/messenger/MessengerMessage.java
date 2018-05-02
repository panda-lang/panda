package org.panda_lang.panda.framework.design.interpreter.messenger;

public interface MessengerMessage {

    enum Level {
        DEBUG,
        INFO,
        WARNING,
        FAILURE
    }

    String[] getContent();

    MessengerMessage.Level getLevel();

}
