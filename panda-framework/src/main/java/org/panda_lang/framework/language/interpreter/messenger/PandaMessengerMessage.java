/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.messenger;

import org.panda_lang.framework.design.interpreter.messenger.MessengerMessage;
import org.slf4j.event.Level;

public final class PandaMessengerMessage implements MessengerMessage {

    private final Level level;
    private final String message;

    public PandaMessengerMessage(Level level, String message) {
        this.message = message;
        this.level = level;
    }

    @Override
    public String getContent() {
        return message;
    }

    @Override
    public Level getLevel() {
        return level;
    }

}
