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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerOutputListener;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatter.MessengerDataFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatter.MessengerDataFormatterManager;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatter.MessengerDataMapper;
import org.panda_lang.panda.framework.design.interpreter.messenger.translator.PandaTranslatorLayout;
import org.panda_lang.panda.framework.design.interpreter.messenger.translator.PandaTranslatorLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class MessengerPandaBootstrap implements PandaBootstrapElement {

    private final PandaBootstrap bootstrap;
    private final Collection<Class<? extends PandaTranslatorLayout<?>>> layouts = new ArrayList<>();
    private final Collection<Class<? extends MessengerDataFormatter<?>>> dataFormatters = new ArrayList<>();
    private final Map<Class<?>, MessengerDataMapper> dataMappers = new HashMap<>();
    private MessengerOutputListener outputListener;

    public MessengerPandaBootstrap(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    @SafeVarargs
    public final MessengerPandaBootstrap withLayouts(Class<? extends PandaTranslatorLayout<?>>... layoutClasses) {
        for (Class<? extends PandaTranslatorLayout<?>> layoutClass : layoutClasses) {
            withLayout(layoutClass);
        }

        return this;
    }

    public MessengerPandaBootstrap withLayout(Class<? extends PandaTranslatorLayout<?>> layoutClass) {
        layouts.add(layoutClass);
        return this;
    }

    @SafeVarargs
    public final MessengerPandaBootstrap withDataFormatters(Class<? extends MessengerDataFormatter<?>>... dataFormatterClasses) {
        for (Class<? extends MessengerDataFormatter<?>> dataFormatterClass : dataFormatterClasses) {
            withDataFormatter(dataFormatterClass);
        }

        return this;
    }

    public MessengerPandaBootstrap withDataFormatter(Class<? extends MessengerDataFormatter<?>> dataFormatterClass) {
        dataFormatters.add(dataFormatterClass);
        return this;
    }

    public MessengerPandaBootstrap withOutputListener(MessengerOutputListener outputListener) {
        this.outputListener = outputListener;
        return this;
    }

    public MessengerPandaBootstrap withDataMapper(MessengerDataMapper<?, ?> dataMapper) {
        this.dataMappers.put(dataMapper.getType(), dataMapper);
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        bootstrap.resources.withMessengerInitializer(messenger -> {
            if (outputListener != null) {
                messenger.setOutputListener(outputListener);
            }

            PandaTranslatorLayoutManager translatorLayoutManager = new PandaTranslatorLayoutManager(messenger, dataMappers);
            layouts.forEach(translatorLayoutManager::load);

            MessengerDataFormatterManager dataFormatterManager = new MessengerDataFormatterManager(messenger);
            dataFormatters.forEach(dataFormatterManager::load);
        });

        return bootstrap;
    }

}
