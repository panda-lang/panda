/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.messenger;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerLevel;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerMessage;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerOutputListener;
import org.slf4j.Logger;

final class DefaultOutputListener implements MessengerOutputListener {

    private final Logger logger;

    DefaultOutputListener(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onMessage(MessengerMessage message) {
        for (String line : message.getContent()) {
            this.log(message.getLevel(), line);
        }
    }

    private void log(MessengerLevel level, String message) {
        switch (level) {
            case DEBUG:
                logger.debug(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            case INFO:
                logger.info(message);
                break;
            case WARNING:
                logger.warn(message);
                break;
            case ERROR:
            case FAILURE:
                logger.error(message);
                break;
        }
    }

}
