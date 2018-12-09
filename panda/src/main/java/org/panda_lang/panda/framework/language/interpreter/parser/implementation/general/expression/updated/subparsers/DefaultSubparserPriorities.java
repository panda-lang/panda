package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

public class DefaultSubparserPriorities {

    private static final double GROUND_ZERO = 0;
    private static final double GROUP_DIFF = 0.01D;

    public static final double ADVANCED_DYNAMIC = GROUND_ZERO + 1;
    public static final double DYNAMIC = ADVANCED_DYNAMIC + 1;
    public static final double SIMPLE_DYNAMIC = DYNAMIC + 1;
    public static final double SINGLE = SIMPLE_DYNAMIC + 1;

    public static class Dynamic {

        public static final double CONSTRUCTOR_CALL = DYNAMIC + GROUP_DIFF;

    }

}
