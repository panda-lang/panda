package org.panda_lang.panda.framework.language.interpreter.messenger.listeners;

import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.messenger.*;

public class DefaultOutputListener implements MessengerOutputListener {

    @Override
    public void onMessage(MessengerMessage message) {
        for (String line : message.getContent()) {
            this.log(message.getLevel(), line);
        }
    }

    private void log(MessengerMessage.Level level, String message) {
        switch (level) {
            case DEBUG:
                PandaFramework.getLogger().debug(message);
                break;
            case INFO:
                PandaFramework.getLogger().info(message);
                break;
            case WARNING:
                PandaFramework.getLogger().warn(message);
                break;
            case FAILURE:
                PandaFramework.getLogger().error(message);
                break;
        }
    }

}
