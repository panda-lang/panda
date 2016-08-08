package org.panda_lang.panda.composition.syntax;

import org.panda_lang.core.interpreter.parser.lexer.suggestion.Sequence;

public class Sequences {

    public static final Sequence STRING = new Sequence("\"");

    public static final Sequence CHARACTER = new Sequence("'");

    private static final Sequence[] VALUES = new Sequence[2];

    static {
        VALUES[0] = STRING;
        VALUES[1] = CHARACTER;
    }

    public static Sequence[] values() {
        return VALUES;
    }

}
