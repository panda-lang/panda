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

package org.panda_lang.panda.design.interpreter.parser;

/**
 * Default list of names used by {@link org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo} for components
 */
public class PandaComponents {

    /**
     * Used by {@link org.panda_lang.panda.Panda}
     */
    public static final String PANDA = "panda";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo}
     */
    public static final String PARENT_INFO = "parent-info";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.architecture.Environment}
     */
    public static final String ENVIRONMENT = "environment";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.architecture.Application}
     */
    public static final String APPLICATION = "application";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.architecture.Script}
     */
    public static final String SCRIPT = "script";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry}
     */
    public static final String PIPELINE_REGISTRY = "pipeline";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream}
     */
    public static final String SOURCE_STREAM = "source-stream";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration}
     */
    public static final String GENERATION = "generation";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration}
     */
    public static final String CURRENT_PARSER_INFO = "current-parsers-info";

    /**
     * Used by {@link org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker}
     */
    public static final String SCOPE_LINKER = "scope-linker";

    /**
     * Used by {@link org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype}
     */
    public static final String CLASS_PROTOTYPE = "class-prototype";

    /**
     * USed by {@link org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry}
     */
    public static final String MODULE_REGISTRY = "module-registry";


}
