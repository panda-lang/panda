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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.language.interpreter.parser.block.BlockSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationSubparser;

import java.util.Arrays;
import java.util.Collection;

/**
 * Used by {@link RegistrableParser}
 */
public final class PandaPipeline {

    /**
     * Text representation of {@link PandaPipeline#BLOCK}
     */
    public static final String BLOCK_LABEL = "block";
    /**
     * Class type parsers, used by {@link org.panda_lang.panda.language.interpreter.parser.block.BlockParser}
     */
    public static final PipelineComponent<BlockSubparser> BLOCK = PipelineComponent.of(BLOCK_LABEL, BlockSubparser.class);

    /**
     * Text representation of {@link PandaPipeline#ASSIGNER}
     */
    public static final String ASSIGNER_LABEL = "assignation";
    /**
     * Assigner parsers, used by {@link org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationExpressionSubparser}
     */
    public static final PipelineComponent<AssignationSubparser> ASSIGNER = PipelineComponent.of(ASSIGNER_LABEL, AssignationSubparser.class);

    /**
     * Get collection of all components
     *
     * @return the collection of components
     */
    public static Collection<PipelineComponent<?>> getPipelineComponents() {
        return Arrays.asList(BLOCK, ASSIGNER);
    }

}
