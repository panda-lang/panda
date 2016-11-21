package org.panda_lang.core.interpreter.parser.util;

import org.panda_lang.core.interpreter.parser.ParserPipeline;

/**
 * Default list of names used by {@link org.panda_lang.core.interpreter.parser.ParserInfo} for components
 */
public class Components {

    /**
     * Used by {@link org.panda_lang.core.interpreter.Interpreter}
     */
    public static final String INTERPRETER = "interpreter";

    /**
     * Used by {@link ParserPipeline}
     */
    public static final String PARSER_PIPELINE = "pipeline";

    /**
     * Used by {@link org.panda_lang.core.interpreter.parser.ParserContext}
     */
    public static final String PARSER_CONTEXT = "context";

    /**
     * Used by {@link org.panda_lang.core.structure.Wrapper}
     */
    public static final String CURRENT_WRAPPER = "current-wrapper"; // TODO: WrapperLinker

}
