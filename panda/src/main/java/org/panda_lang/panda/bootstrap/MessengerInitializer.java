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

package org.panda_lang.panda.bootstrap;

import org.panda_lang.language.interpreter.messenger.MessengerOutputListener;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatterManager;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataMapper;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayoutManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * {@link org.panda_lang.language.interpreter.messenger.Messenger} creator
 */
public final class MessengerInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final Collection<Supplier<Collection<PandaTranslatorLayout<?>>>> layouts = new ArrayList<>(2);
    private final Collection<Supplier<Collection<MessengerDataFormatter<?>>>> dataFormatters = new ArrayList<>(2);
    private final Map<Class<?>, MessengerDataMapper<?, ?>> dataMappers = new HashMap<>(2);
    private MessengerOutputListener outputListener;

    MessengerInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Add translator layouts to messenger
     *
     * @param layouts layouts to add
     * @return the category instance
     */
    public final MessengerInitializer addLayouts(Supplier<Collection<PandaTranslatorLayout<?>>> layouts) {
        this.layouts.add(layouts);
        return this;
    }

    /**
     * Add data formatters to messenger
     *
     * @param dataFormatters classes of formatters to add
     * @return the category instance
     */
    public final MessengerInitializer addDataFormatters(Supplier<Collection<MessengerDataFormatter<?>>> dataFormatters) {
        this.dataFormatters.add(dataFormatters);
        return this;
    }

    /**
     * Set custom output listener (by default listener is assigned to the logger)
     *
     * @param outputListener the listener to use
     * @return the category instance
     */
    public MessengerInitializer withOutputListener(MessengerOutputListener outputListener) {
        this.outputListener = outputListener;
        return this;
    }

    /**
     * Add data mapper to messenger
     *
     * @param dataMapper the mapper to add
     * @return the category instance
     */
    public MessengerInitializer addDataMapper(MessengerDataMapper<?, ?> dataMapper) {
        this.dataMappers.put(dataMapper.getType(), dataMapper);
        return this;
    }

    @Override
    public PandaBootstrap collect() {
        if (outputListener != null) {
            bootstrap.resources.withOutputListener(outputListener);
        }

        bootstrap.resources.withMessengerInitializer(messenger -> {
            PandaTranslatorLayoutManager translatorLayoutManager = new PandaTranslatorLayoutManager(messenger, dataMappers);
            layouts.stream()
                    .flatMap(layout -> layout.get().stream())
                    .forEach(translatorLayoutManager::load);

            MessengerDataFormatterManager dataFormatterManager = new MessengerDataFormatterManager(messenger);
            dataFormatters.stream()
                    .flatMap(layout -> layout.get().stream())
                    .forEach(dataFormatterManager::load);
        });

        return bootstrap;
    }

}
