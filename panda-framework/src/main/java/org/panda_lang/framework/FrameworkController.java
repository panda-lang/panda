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

package org.panda_lang.framework;

import org.panda_lang.framework.design.interpreter.messenger.LoggerHolder;
import org.panda_lang.framework.design.resource.Language;
import org.panda_lang.framework.design.resource.Resources;
import org.slf4j.Logger;

/**
 * Panda Framework controller
 */
public interface FrameworkController extends LoggerHolder {

    /**
     * Get resources used by framework controller
     *
     * @return the resources
     */
    Resources getResources();

    /**
     * Get language used by framework controller
     *
     * @return the language
     */
    Language getLanguage();

    /**
     * Get the current version of framework controller
     *
     * @return the current version
     */
    String getVersion();

    /**
     * Get logger used by the framework
     *
     * @return the current logger
     */
    @Override
    Logger getLogger();

}
