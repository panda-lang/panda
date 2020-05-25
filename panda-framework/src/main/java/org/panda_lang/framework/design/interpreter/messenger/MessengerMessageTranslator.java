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

/**
 * Translates object of the specified type into the messages
 *
 * @param <T> type of accepted objects
 */
public interface MessengerMessageTranslator<T extends Object> {

    /**
     * Translate object into the messages
     *
     * @param messenger the messenger to use
     * @param element the object to translate
     * @return true if message interrupts current process (e.g. exceptions), otherwise false (e.g. warnings)
     */
    boolean translate(Messenger messenger, T element);

    /**
     * Get type of accepted values
     *
     * @return the type
     */
    Class<T> getType();

}
