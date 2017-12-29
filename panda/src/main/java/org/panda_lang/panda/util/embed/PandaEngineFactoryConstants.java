/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.util.embed;

import org.panda_lang.panda.PandaConstants;

import java.util.ArrayList;
import java.util.List;

public class PandaEngineFactoryConstants {

    /**
     * Language name
     */
    public static final String NAME = "Panda";

    /**
     * Panda version
     */
    public static final String VERSION = PandaConstants.VERSION;

    /**
     * Engine name
     */
    public static final String ENGINE_NAME = "Panda Engine";

    /**
     * Engine version
     */
    public static final String ENGINE_VERSION = VERSION;

    /**
     * Extensions
     */
    public static final List<String> EXTENSIONS = new ArrayList<>();

    /**
     * Mime types
     */
    public static final List<String> MIME_TYPES = new ArrayList<>();


    /**
     * Names for Panda Engine
     */
    public static final List<String> NAMES = new ArrayList<>();


    static {
        EXTENSIONS.add(".panda");

        MIME_TYPES.add("application/panda");
        MIME_TYPES.add("text/panda");

        NAMES.add(NAME);
        NAMES.add(ENGINE_NAME);
    }

}
