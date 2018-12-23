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

package org.panda_lang.panda.framework.design.interpreter.parser;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.panda.framework.design.resource.Autoload;
import org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block.BlockSubparser;

/**
 * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration}
 */
@Autoload
public class PandaPipelines {

    /**
     * Text representation of {@link PandaPipelines#PROTOTYPE}
     */
    public static final String PROTOTYPE_LABEL = "prototype";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.interpreter.parser.prototype.ClassPrototypeParser}
     */
    public static final PipelineComponent<UnifiedParser> PROTOTYPE = PipelineComponent.of(PROTOTYPE_LABEL, UnifiedParser.class);

    /**
     * Text representation of {@link PandaPipelines#SCOPE}
     */
    public static final String SCOPE_LABEL = "scope";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.interpreter.parser.ScopeParser}
     */
    public static final PipelineComponent<UnifiedParser> SCOPE = PipelineComponent.of(SCOPE_LABEL, UnifiedParser.class);

    /**
     * Text representation of {@link PandaPipelines#BLOCK}
     */
    public static final String BLOCK_LABEL = "block";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block.BlockParser}
     */
    public static final PipelineComponent<BlockSubparser> BLOCK = PipelineComponent.of(BLOCK_LABEL, BlockSubparser.class);

    /**
     * Text representation of {@link PandaPipelines#STATEMENT}
     */
    public static final String STATEMENT_LABEL = "statement";
    /**
     * Class prototype parsers, used by {@link org.panda_lang.panda.framework.language.interpreter.parser.statement.StatementParser}
     */
    public static final PipelineComponent<UnifiedParser> STATEMENT = PipelineComponent.of(STATEMENT_LABEL, UnifiedParser.class);

}
