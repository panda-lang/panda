package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.messenger.*;

public class PandaMessengerMessage implements MessengerMessage {

    private final Level level;
    private final String[] message;

    public PandaMessengerMessage(Level level, String... message) {
        this.message = message;
        this.level = level;
    }

    @Override
    public String[] getContent() {
        return message;
    }

    @Override
    public Level getLevel() {
        return level;
    }

}
