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

package org.panda_lang.language.interpreter.parser.pool;

import org.panda_lang.language.interpreter.parser.ContextParser;

import java.util.Arrays;
import java.util.Collection;

/**
 * Default pipelines used by the framework
 */
public final class Targets {

    /**
     * Text representation of {@link Targets#ALL}
     */
    public static final String ALL_LABEL = "all";
    /**
     * All pipelines
     */
    public static final Target<ContextParser> ALL = Target.of(ALL_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Targets#HEAD}
     */
    public static final String HEAD_LABEL = "head";
    /**
     * Head pipeline
     */
    public static final Target<ContextParser> HEAD = Target.of(HEAD_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Targets#TYPE}
     */
    public static final String TYPE_LABEL = "type";
    /**
     * Class type parsers, used by type parser
     */
    public static final Target<ContextParser> TYPE = Target.of(TYPE_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Targets#SCOPE}
     */
    public static final String SCOPE_LABEL = "scope";
    /**
     * Scope parsers
     */
    public static final Target<ContextParser> SCOPE = Target.of(SCOPE_LABEL, ContextParser.class);

    /**
     * Get collection of all components
     *
     * @return the collection of components
     */
    public static Collection<Target<?>> getPipelineComponents() {
        return Arrays.asList(ALL, HEAD, TYPE, SCOPE);
    }

}
