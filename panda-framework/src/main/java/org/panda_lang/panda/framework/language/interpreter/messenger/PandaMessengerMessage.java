package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.messenger.*;

public class PandaMessengerMessage implements MessengerMessage {

    private final String title;
    private final String messageContent;
    private final Level level;

    public PandaMessengerMessage(String title, String messageContent, Level level) {
        this.title = title;
        this.messageContent = messageContent;
        this.level = level;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public Level getLevel() {
        return level;
    }

}
