/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.design.interpreter.messenger;

import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * Logging center
 */
public interface Messenger extends LoggerHolder {

    /**
     * Send object through the messenger
     *
     * @param message the message-object to send
     * @return true if message breaks the process of interpretation/execution
     */
    boolean send(Object message);

    /**
     * Send standard text message through messenger
     *
     * @param level the level of message
     * @param message the message to send
     */
    void send(Level level, Object message);

    /**
     * Send messenger message through messenger.
     *
     * @param message the message to send
     *
     * @see org.panda_lang.framework.design.interpreter.messenger.Messenger#send(Level, java.lang.Object)
     */
    void send(MessengerMessage message);

    /**
     * Add message translator associated with the specified type
     *
     * @param translator the translator to register
     */
    void addMessageTranslator(MessengerMessageTranslator<Object> translator);

    /**
     * Change output listener.
     * Default output listener sends messages through {@link org.panda_lang.framework.PandaFramework#getLogger()}
     *
     * @param listener the listener to set
     */
    void setOutputListener(MessengerOutputListener listener);

    /**
     * Get formatter used by messenger
     *
     * @return the formatter instance
     */
    MessengerFormatter getMessengerFormatter();

    /**
     * Get assigned to messenger logger
     *
     * @return the assigned logger
     */
    @Override
    Logger getLogger();

}
