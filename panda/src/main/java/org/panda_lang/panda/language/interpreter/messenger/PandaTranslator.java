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
import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.messenger.MessengerMessage;
import org.panda_lang.framework.design.interpreter.messenger.MessengerMessageTranslator;
import org.panda_lang.framework.language.interpreter.messenger.PandaMessengerMessage;
import org.panda_lang.panda.language.interpreter.messenger.template.MicroTemplate;
import org.panda_lang.panda.language.interpreter.messenger.template.MicroTemplateEngine;
import org.panda_lang.panda.language.interpreter.messenger.template.MicroTemplateRequest;

import java.util.HashMap;
import java.util.Map;

final class PandaTranslator<T> implements MessengerMessageTranslator<T> {

    private final MicroTemplateEngine engine;
    private final PandaTranslatorLayout<T> scheme;
    private final Map<Class<?>, MessengerDataMapper> mappers;

    PandaTranslator(MicroTemplateEngine engine, PandaTranslatorLayout<T> scheme, Map<Class<?>, MessengerDataMapper> mappers) {
        this.engine = engine;
        this.scheme = scheme;
        this.mappers = mappers;
    }

    @Override
    public boolean translate(Messenger messenger, T element) {
        Map<String, Object> data = new HashMap<>();
        data.put(null, element);

        MessengerFormatter formatter = messenger.getMessengerFormatter().fork();
        scheme.onHandle(formatter, element, data);

        HashMap<String, Object> patch = new HashMap<>();
        data.forEach((key, value) -> {
            if (value == null || !mappers.containsKey(value.getClass())) {
                return;
            }

            //noinspection unchecked
            patch.put(key, mappers.get(value.getClass()).apply(value));
        });
        data.putAll(patch);

        MicroTemplateRequest request = MicroTemplateRequest.builder()
                .withSource(scheme.getTemplateSource())
                .withFormatter(formatter)
                .withData(data)
                .withPrefix(scheme.getPrefix())
                .build();

        MicroTemplate template = engine.load(request);
        MessengerMessage message = new PandaMessengerMessage(scheme.getLevel(), template.getContent());
        messenger.sendMessage(message);

        return scheme.isInterrupting();
    }

    @Override
    public Class<T> getType() {
        return scheme.getType();
    }

}
