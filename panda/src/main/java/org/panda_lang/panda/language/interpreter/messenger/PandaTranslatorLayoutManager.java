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

import org.panda_lang.panda.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.panda.language.interpreter.messenger.template.MicroTemplateEngine;

import java.util.Map;

public final class PandaTranslatorLayoutManager {

    private final Messenger messenger;
    private final Map<Class<?>, MessengerDataMapper> mappers;
    private final MicroTemplateEngine engine = new MicroTemplateEngine();

    public PandaTranslatorLayoutManager(Messenger messenger, Map<Class<?>, MessengerDataMapper> mappers) {
        this.messenger = messenger;
        this.mappers = mappers;
    }

    @SafeVarargs
    public final void load(Class<? extends PandaTranslatorLayout<?>>... classes) {
        for (Class<? extends PandaTranslatorLayout<?>> layout : classes) {
            load(layout);
        }
    }

    public void load(Class<? extends PandaTranslatorLayout<?>> layout) {
        try {
            PandaTranslatorLayout<?> layoutInstance = layout.newInstance();
            load(layoutInstance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot create instance of layout " + layout, e);
        }
    }

    public <T extends PandaTranslatorLayout<G>, G> T load(T layout) {
        PandaTranslator<G> translator = new PandaTranslator<>(engine, layout, mappers);
        messenger.addMessageTranslator(translator);
        return layout;
    }

}
