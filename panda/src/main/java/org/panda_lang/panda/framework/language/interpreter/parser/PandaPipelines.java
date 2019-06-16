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

package org.panda_lang.panda.framework.language.interpreter.parser;

import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.panda.framework.design.resource.Autoload;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockSubparser;

/**
 * Used by {@link org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration}
 */
@Autoload
public class PandaPipelines implements Pipelines {

    /**
     * Text representation of {@link PandaPipelines#PROTOTYPE}
     */
    public static final String PROTOTYPE_LABEL = "prototype";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.resource.parsers.overall.prototype.ClassPrototypeParser}
     */
    public static final PipelineComponent<UnifiedParser> PROTOTYPE = PipelineComponent.of(PandaPipelines.class, PROTOTYPE_LABEL, UnifiedParser.class);

    /**
     * Text representation of {@link PandaPipelines#CONTAINER}
     */
    public static final String CONTAINER_LABEL = "container";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.resource.parsers.ContainerParser}
     */
    public static final PipelineComponent<UnifiedParser> CONTAINER = PipelineComponent.of(PandaPipelines.class, CONTAINER_LABEL, UnifiedParser.class);

    /**
     * Text representation of {@link PandaPipelines#BLOCK}
     */
    public static final String BLOCK_LABEL = "block";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.resource.parsers.container.block.BlockParser}
     */
    public static final PipelineComponent<BlockSubparser> BLOCK = PipelineComponent.of(PandaPipelines.class, BLOCK_LABEL, BlockSubparser.class);

    /**
     * Text representation of {@link PandaPipelines#ASSIGNER}
     */
    public static final String ASSIGNER_LABEL = "assignation";
    /**
     * Assigner parsers, used by {@link org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationParser}
     */
    public static final PipelineComponent<AssignationSubparser> ASSIGNER = PipelineComponent.of(PandaPipelines.class, ASSIGNER_LABEL, AssignationSubparser.class);

}
