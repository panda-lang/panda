package org.panda_lang.framework.interpreter.parser.util;

import org.panda_lang.framework.interpreter.parser.linker.WrapperLinker;

/**
 * Default list of names used by {@link org.panda_lang.framework.interpreter.parser.ParserInfo} for components
 */
public class Components {

    /**
     * Used by {@link org.panda_lang.framework.interpreter.Interpreter}
     */
    public static final String INTERPRETER = "interpreter";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.parser.ParserPipeline}
     */
    public static final String PARSER_PIPELINE = "pipeline";

    /**
     * Used by {@link org.panda_lang.framework.interpreter.lexer.TokenReader}
     */
    public static final String READER = "reader";

    /**
     * Used by {@link WrapperLinker}
     */
    public static final String LINKER = "linker";

    /**
     * Array of the default names
     */
    private static final String[] VALUES = new String[4];

    static {
        VALUES[0] = INTERPRETER;
        VALUES[1] = PARSER_PIPELINE;
        VALUES[2] = READER;
        VALUES[3] = LINKER;
    }

    public static String[] values() {
        return VALUES;
    }

}
