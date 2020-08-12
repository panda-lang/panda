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

import org.panda_lang.language.resource.Syntax;
import org.panda_lang.language.resource.PandaLanguage;
import org.panda_lang.language.resource.PandaLanguage.PandaLanguageBuilder;
import org.panda_lang.language.resource.PandaResources;
import org.panda_lang.language.resource.PandaResources.PandaResourcesBuilder;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.Panda.PandaBuilder;
import org.slf4j.Logger;

/**
 * Utility class to simplify initialization process of Panda
 */
public final class PandaBootstrap {

    protected final Logger logger;
    protected final PandaBuilder panda = Panda.builder();
    protected final PandaLanguageBuilder language = PandaLanguage.builder();
    protected final PandaResourcesBuilder resources = PandaResources.builder();

    PandaBootstrap(Logger logger) {
        this.logger = logger;
    }

    /**
     * Define syntax used by Panda
     *
     * @param syntax the syntax to use
     * @return the bootstrap instance
     */
    public PandaBootstrap withSyntax(Syntax syntax) {
        this.language.withSyntax(syntax);
        return this;
    }

    /**
     * Create parsers initializer
     *
     * @return the initializer
     */
    public ParsersInitializer initializeParsers() {
        return new ParsersInitializer(this);
    }

    /**
     * Get pipelines initializer
     *
     * @return the initializer
     */
    public PipelinesInitializer initializePipelines() {
        return new PipelinesInitializer(this);
    }

    /**
     * Get messenger initializer
     *
     * @return the initializer
     */
    public MessengerInitializer initializeMessenger() {
        return new MessengerInitializer(this);
    }

    /**
     * Create panda based on the collected data
     *
     * @return a new panda instance
     */
    public Panda create() {
        return panda
                .withLogger(logger)
                .withLanguage(language.build())
                .withResources(resources.build())
                .build();
    }

    /**
     * Initialize bootstrap
     *
     * @param logger the logger to use by bootstrap and panda
     * @return a new bootstrap instance
     */
    public static PandaBootstrap initializeBootstrap(Logger logger) {
        return new PandaBootstrap(logger);
    }

}