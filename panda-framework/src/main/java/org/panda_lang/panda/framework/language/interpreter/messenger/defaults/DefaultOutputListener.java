/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.language.interpreter.messenger.defaults;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerMessage;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerOutputListener;

public class DefaultOutputListener implements MessengerOutputListener {

    @Override
    public void onMessage(MessengerMessage message) {
        for (String line : message.getContent()) {
            this.log(message.getLevel(), line);
        }
    }

    private void log(MessengerLevel level, String message) {
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
