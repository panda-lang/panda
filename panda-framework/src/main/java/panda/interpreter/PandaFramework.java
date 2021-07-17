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

package panda.interpreter;

import panda.interpreter.logging.Logger;
import panda.interpreter.resource.Language;
import panda.interpreter.resource.Resources;

public abstract class PandaFramework implements FrameworkController {

    private final Logger logger;
    private final PandaClassLoader classLoader;
    private final Language language;
    private final Resources resources;

    protected PandaFramework(Logger logger, PandaClassLoader classLoader, Language language, Resources resources) {
        if (logger == null) {
            throw new IllegalArgumentException("Missing logger");
        }

        if (language == null) {
            throw new IllegalArgumentException("Missing language");
        }

        if (resources == null) {
            throw new IllegalArgumentException("Missing resources");
        }

        this.logger = logger;
        this.classLoader = classLoader;
        this.language = language;
        this.resources = resources;
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

    @Override
    public PandaClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get current version of Panda
     *
     * @return the current version
     */
    @Override
    public String getVersion() {
        return PandaFrameworkConstants.VERSION;
    }

}
