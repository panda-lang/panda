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

import org.panda_lang.framework.design.interpreter.messenger.MessengerOutputListener;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatterManager;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataMapper;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayout;
import org.panda_lang.panda.language.interpreter.messenger.PandaTranslatorLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link org.panda_lang.framework.design.interpreter.messenger.Messenger} creator
 */
public final class MessengerInitializer implements Initializer {

    private final PandaBootstrap bootstrap;
    private final Collection<Class<? extends PandaTranslatorLayout<?>>> layouts = new ArrayList<>();
    private final Collection<Class<? extends MessengerDataFormatter<?>>> dataFormatters = new ArrayList<>();
    private final Map<Class<?>, MessengerDataMapper> dataMappers = new HashMap<>();
    private MessengerOutputListener outputListener;

    MessengerInitializer(PandaBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    /**
     * Add translator layouts to messenger
     *
     * @param layoutClasses classes of layouts to add
     * @return the category instance
     */
    @SafeVarargs
    public final MessengerInitializer addLayouts(Class<? extends PandaTranslatorLayout<?>>... layoutClasses) {
        layouts.addAll(Arrays.asList(layoutClasses));
        return this;
    }

    /**
     * Add data formatters to messenger
     *
     * @param dataFormatterClasses classes of formatters to add
     * @return the category instance
     */
    @SafeVarargs
    public final MessengerInitializer addDataFormatters(Class<? extends MessengerDataFormatter<?>>... dataFormatterClasses) {
        dataFormatters.addAll(Arrays.asList(dataFormatterClasses));
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
