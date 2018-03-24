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

package org.panda_lang.panda.design.interpreter.parser.util;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;

/**
 * Default list of names used by {@link ParserInfo} for components
 */
public class Components {

    /**
     * Used by {@link org.panda_lang.panda.Panda}
     */
    public static final String PANDA = "panda";

    /**
     * Used by {@link ParserInfo}
     */
    public static final String PARENT_INFO = "parent-info";

    /**
     * Used by {@link Interpreter}
     */
    public static final String INTERPRETER = "interpreter";

    /**
     * Used by {@link Script}
     */
    public static final String SCRIPT = "script";

    /**
     * Used by {@link ParserPipelineRegistry}
     */
    public static final String PIPELINE_REGISTRY = "pipeline";

    /**
     * Used by {@link SourceStream}
     */
    public static final String SOURCE_STREAM = "source-stream";

    /**
     * Used by {@link CasualParserGeneration}
     */
    public static final String GENERATION = "generation";

    /**
     * Used by {@link CasualParserGeneration}
     */
    public static final String CURRENT_PARSER_INFO = "current-parsers-info";

    /**
     * Used by {@link ScopeLinker}
     */
    public static final String SCOPE_LINKER = "scope-linker";

    /**
     * Used by {@link ClassPrototype}
     */
    public static final String CLASS_PROTOTYPE = "class-prototype";

    /**
     * Array of the default names
     */
    private static final String[] VALUES = new String[7];

    static {
        VALUES[0] = PARENT_INFO;
        VALUES[1] = INTERPRETER;
        VALUES[2] = PIPELINE_REGISTRY;
        VALUES[3] = SOURCE_STREAM;
        VALUES[4] = GENERATION;
        VALUES[5] = CURRENT_PARSER_INFO;
        VALUES[6] = SCOPE_LINKER;
    }

    public static String[] values() {
        return VALUES;
    }

}
