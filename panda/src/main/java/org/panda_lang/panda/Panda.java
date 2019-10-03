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

package org.panda_lang.panda;

import org.panda_lang.panda.cli.PandaCLI;
import org.panda_lang.framework.design.FrameworkController;
import org.panda_lang.framework.design.resource.Language;
import org.panda_lang.framework.design.resource.Resources;
import org.panda_lang.framework.language.resource.PandaLanguage;
import org.panda_lang.framework.language.resource.PandaResources;
import org.panda_lang.panda.language.interpreter.PandaFileLoader;
import org.panda_lang.panda.util.embed.PandaEngineFactoryConstants;

/**
 * The framework controller of Panda language.
 *
 * @see org.panda_lang.panda.Panda.PandaBuilder
 *
 */
public final class Panda implements FrameworkController {

    private final Language language;
    private final Resources resources;

    private final PandaCLI cli;
    private final PandaFileLoader loader;

    private Panda(PandaBuilder builder) {
        if (builder.language == null) {
            throw new IllegalArgumentException("Language has to be defined");
        }

        if (builder.resources == null) {
            throw new IllegalArgumentException("Pipeline path has to be defined");
        }

        this.language = builder.language;
        this.resources = builder.resources;

        this.cli = new PandaCLI(this);
        this.loader = new PandaFileLoader(this);
    }

    /**
     * Get CLI used to command-line communication
     *
     * @return the panda cli
     */
    public PandaCLI getCli() {
        return cli;
    }

    /**
     * Get loader used to load applications
     *
     * @return the panda loader
     */
    public PandaFileLoader getLoader() {
        return loader;
    }

    /**
     * Get resources used by this Panda instance
     *
     * @return the panda resources
     */
    @Override
    public Resources getResources() {
        return resources;
    }

    /**
     * Get language used by this Panda instance
     *
     * @return the panda language
     */
    @Override
    public Language getLanguage() {
        return language;
    }

    /**
     * Get current version of Panda
     *
     * @return the current version
     */
    @Override
    public String getVersion() {
        return PandaEngineFactoryConstants.VERSION;
    }

    public static PandaBuilder builder() {
        return new PandaBuilder();
    }

    public static class PandaBuilder {

        protected PandaLanguage language;
        protected PandaResources resources;

        private PandaBuilder() { }

        public PandaBuilder withLanguage(PandaLanguage language) {
            this.language = language;
            return this;
        }

        public PandaBuilder withResources(PandaResources resources) {
            this.resources = resources;
            return this;
        }

        public Panda build() {
            return new Panda(this);
        }

        public static PandaBuilder builder() {
            return new PandaBuilder();
        }

    }

}
