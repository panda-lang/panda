/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.parser.util;

/**
 * Default list of names used by {@link org.panda_lang.framework.interpreter.parser.ParserInfo} for components
 */
public class Components {

    /**
     * Used by {@link org.panda_lang.framework.interpreter.parser.ParserInfo}
     */
    public static final String FORKED = "forked";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.Interpreter}
     */
    public static final String INTERPRETER = "interpreter";

    /**
     * Used by {@link org.panda_lang.panda.implementation.structure.Script}
     */
    public static final String SCRIPT = "script";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.parser.pipeline.registry.PipelineRegistry}
     */
    public static final String PIPELINE_REGISTRY = "pipeline";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.lexer.token.distributor.SourceStream}
     */
    public static final String SOURCE_STREAM = "source-stream";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.parser.generation.ParserGeneration}
     */
    public static final String GENERATION = "generation";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.parser.generation.ParserGeneration}
     */
    public static final String CURRENT_PARSER_INFO = "current-parser-info";

    /**
     * Used by {@link org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker}
     */
    public static final String LINKER = "linker";

    /**
     * Array of the default names
     */
    private static final String[] VALUES = new String[7];

    static {
        VALUES[0] = FORKED;
        VALUES[1] = INTERPRETER;
        VALUES[2] = PIPELINE_REGISTRY;
        VALUES[3] = SOURCE_STREAM;
        VALUES[4] = GENERATION;
        VALUES[5] = CURRENT_PARSER_INFO;
        VALUES[6] = LINKER;
    }

    public static String[] values() {
        return VALUES;
    }

}
