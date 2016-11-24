package org.panda_lang.core.interpreter.parser.util;

import org.panda_lang.core.interpreter.parser.linker.WrapperLinker;

/**
 * Default list of names used by {@link org.panda_lang.core.interpreter.parser.ParserInfo} for components
 */
public class Components {

    /**
     * Used by {@link org.panda_lang.core.interpreter.Interpreter}
     */
    public static final String INTERPRETER = "interpreter";

    /**
     * Used by {@link org.panda_lang.core.interpreter.parser.ParserPipeline}
     */
    public static final String PARSER_PIPELINE = "pipeline";

    /**
     * Used by {@link org.panda_lang.core.interpreter.parser.ParserContext}
     */
    public static final String PARSER_CONTEXT = "context";

    /**
     * Used by {@link WrapperLinker}
     */
    public static final String WRAPPER_LINKER = "wrapper-linker";

    /**
     * Array of the default names
     */
    private static final String[] VALUES = new String[4];

    static {
        VALUES[0] = INTERPRETER;
        VALUES[1] = PARSER_PIPELINE;
        VALUES[2] = PARSER_CONTEXT;
        VALUES[3] = WRAPPER_LINKER;
    }

    public static String[] values() {
        return VALUES;
    }

}
