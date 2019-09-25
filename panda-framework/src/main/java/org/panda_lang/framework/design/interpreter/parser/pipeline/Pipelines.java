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

package org.panda_lang.framework.design.interpreter.parser.pipeline;

import org.panda_lang.framework.design.interpreter.parser.ContextParser;

public final class Pipelines implements PipelineComponents {

    /**
     * Text representation of {@link Pipelines#ALL}
     */
    public static final String ALL_LABEL = "all";
    /**
     * All pipelines
     */
    public static final PipelineComponent<ContextParser> ALL = PipelineComponent.of(Pipelines.class, ALL_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Pipelines#HEAD}
     */
    public static final String HEAD_LABEL = "head";
    /**
     * Head pipeline
     */
    public static final PipelineComponent<ContextParser> HEAD = PipelineComponent.of(Pipelines.class, HEAD_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Pipelines#PROTOTYPE}
     */
    public static final String PROTOTYPE_LABEL = "prototype";
    /**
     * Class prototype parsers, used by prototype parser
     */
    public static final PipelineComponent<ContextParser> PROTOTYPE = PipelineComponent.of(Pipelines.class, PROTOTYPE_LABEL, ContextParser.class);

    /**
     * Text representation of {@link Pipelines#SCOPE}
     */
    public static final String SCOPE_LABEL = "scope";
    /**
     * Container parsers
     */
    public static final PipelineComponent<ContextParser> SCOPE = PipelineComponent.of(Pipelines.class, SCOPE_LABEL, ContextParser.class);

}
