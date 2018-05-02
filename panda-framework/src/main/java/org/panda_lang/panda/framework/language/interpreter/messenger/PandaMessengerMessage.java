package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.messenger.*;

public class PandaMessengerMessage implements MessengerMessage {

    private final String message;
    private final String content;
    private final Level level;

    public PandaMessengerMessage(Level level, String message, String content) {
        this.message = message;
        this.content = content;
        this.level = level;
    }

    public PandaMessengerMessage(Level level, String message) {
        this(level, message, null);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDetails() {
        return content;
    }

    @Override
    public Level getLevel() {
        return level;
    }

}
