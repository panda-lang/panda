package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

public class DefaultSubparserPriorities {

    public static final double DYNAMIC = 1;

    public static final double SINGLE = 2;

    public static class Dynamic {

        public static final double CONSTRUCTOR_CALL = DYNAMIC + 0.01D;

    }

}
