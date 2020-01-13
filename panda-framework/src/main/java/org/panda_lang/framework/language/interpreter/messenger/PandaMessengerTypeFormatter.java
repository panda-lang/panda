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

import org.panda_lang.framework.design.interpreter.messenger.FormatterFunction;
import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.messenger.MessengerTypeFormatter;

final class PandaMessengerTypeFormatter<T> implements MessengerTypeFormatter<T> {

    private final Class<T> type;
    private final MessengerFormatter formatter;

    public PandaMessengerTypeFormatter(MessengerFormatter formatter, Class<T> type) {
        this.type = type;
        this.formatter = formatter;
    }

    @Override
    public PandaMessengerTypeFormatter<T> register(String placeholder, FormatterFunction<T> replacementFunction) {
        formatter.register(placeholder, getType(), replacementFunction);
        return this;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

}
