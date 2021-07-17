/*
 * Copyright (c) 2021 dzikoysk
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

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.PandaClassLoader;
import org.panda_lang.framework.PandaFramework;
import org.panda_lang.framework.interpreter.logging.Logger;
import org.panda_lang.framework.resource.Language;
import org.panda_lang.framework.resource.Resources;
import panda.utilities.UnsafeUtils;

/**
 * The framework controller of Panda language.
 *
 * @see Panda#builder()
 */
public final class Panda extends PandaFramework implements FrameworkController {

    private final PandaFileLoader loader = new PandaFileLoader(this);

    static {
        UnsafeUtils.disableIllegalAccessMessage();
    }

    private Panda(PandaBuilder builder) {
        super(builder.logger, new PandaClassLoader(Panda.class.getClassLoader()), builder.language, builder.resources);
    }

    /**
     * Get loader used to load applications
     *
     * @return the panda file loader
     */
    public PandaFileLoader getLoader() {
        return loader;
    }

    /**
     * Create instance of panda builder
     *
     * @return the builder instance
     */
    public static PandaBuilder builder() {
        return new PandaBuilder();
    }

    /**
     * Utility builder
     */
    public static final class PandaBuilder {

        protected Language language;
        protected Resources resources;
        protected Logger logger;

        private PandaBuilder() { }

        public PandaBuilder withLanguage(Language language) {
            this.language = language;
            return this;
        }

        public PandaBuilder withResources(Resources resources) {
            this.resources = resources;
            return this;
        }

        public PandaBuilder withLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Create Panda instance based on the specified in builder values
         *
         * @return a new panda instance
         */
        public Panda build() {
            return new Panda(this);
        }

    }

}
