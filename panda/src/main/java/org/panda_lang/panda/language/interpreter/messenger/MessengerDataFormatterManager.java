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

package org.panda_lang.panda.language.interpreter.messenger;

import org.panda_lang.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.framework.design.interpreter.messenger.MessengerTypeFormatter;

import java.util.Collection;

public final class MessengerDataFormatterManager {

    private final Messenger messenger;

    public MessengerDataFormatterManager(Messenger messenger) {
        this.messenger = messenger;
    }

    public MessengerDataFormatterManager load(Collection<Class<? extends MessengerDataFormatter<?>>> formatterClasses) {
        for (Class<? extends MessengerDataFormatter<?>> formatterClass : formatterClasses) {
            load(formatterClass);
        }

        return this;
    }

    public MessengerDataFormatterManager load(Class<? extends MessengerDataFormatter<?>> formatterClass) {
        try {
            return load(formatterClass.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Cannot create instance of formatter class", e);
        }
    }

    public <T> MessengerDataFormatterManager load(MessengerDataFormatter<T> formatter) {
        MessengerTypeFormatter<T> typeFormatter = messenger.getMessengerFormatter().getTypeFormatter(formatter.getType());
        formatter.onInitialize(typeFormatter);
        return this;
    }

}
