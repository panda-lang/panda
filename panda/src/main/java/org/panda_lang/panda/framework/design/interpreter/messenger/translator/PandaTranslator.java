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

package org.panda_lang.panda.framework.design.interpreter.messenger.translator;

import org.panda_lang.panda.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerMessage;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerMessageTranslator;
import org.panda_lang.panda.framework.language.interpreter.messenger.PandaMessengerMessage;
import org.panda_lang.panda.utilities.commons.CharacterUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

final class PandaTranslator<T> implements MessengerMessageTranslator<T> {


    private final PandaTranslatorLayout<T> scheme;

    PandaTranslator(PandaTranslatorLayout<T> scheme) {
        this.scheme = scheme;
    }

    @Override
    public boolean handle(Messenger messenger, T element) {
        MessengerFormatter formatter = messenger.getMessengerFormatter().fork();
        scheme.onHandle(formatter, element);

        String content = formatter.format(scheme.getTemplateSource().getContent(), element);
        String[] lines = StringUtils.split(content, System.lineSeparator());

        for (int i = 0; i < lines.length; i++) {
            lines[i] = CharacterUtils.BACKSPACE + scheme.getPrefix() + lines[i];
        }

        MessengerMessage message = new PandaMessengerMessage(scheme.getLevel(), lines);
        messenger.sendMessage(message);

        return scheme.isInterrupting();
    }

    @Override
    public Class<T> getType() {
        return scheme.getType();
    }

}
