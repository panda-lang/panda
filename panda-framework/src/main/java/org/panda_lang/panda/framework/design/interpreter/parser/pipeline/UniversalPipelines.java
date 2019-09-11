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

package org.panda_lang.panda.framework.design.interpreter.parser.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.ContextParser;

public final class UniversalPipelines implements Pipelines {

    /**
     * Text representation of {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines#ALL}
     */
    public static final String ALL_LABEL = "all";
    /**
     * All pipelines
     */
    public static final PipelineComponent<ContextParser> ALL = PipelineComponent.of(UniversalPipelines.class, ALL_LABEL, ContextParser.class);

    /**
     * Text representation of {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines#HEAD}
     */
    public static final String HEAD_LABEL = "head";
    /**
     * Head pipeline
     */
    public static final PipelineComponent<ContextParser> HEAD = PipelineComponent.of(UniversalPipelines.class, HEAD_LABEL, ContextParser.class);

    /**
     * Text representation of {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines#PROTOTYPE}
     */
    public static final String PROTOTYPE_LABEL = "prototype";
    /**
     * Class prototype parsers, used by prototype parser
     */
    public static final PipelineComponent<ContextParser> PROTOTYPE = PipelineComponent.of(UniversalPipelines.class, PROTOTYPE_LABEL, ContextParser.class);

    /**
     * Text representation of {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines#SCOPE}
     */
    public static final String SCOPE_LABEL = "scope";
    /**
     * Container parsers, used by {@link org.panda_lang.panda.language.resource.parsers.ScopeParser}
     */
    public static final PipelineComponent<ContextParser> SCOPE = PipelineComponent.of(UniversalPipelines.class, SCOPE_LABEL, ContextParser.class);

}
