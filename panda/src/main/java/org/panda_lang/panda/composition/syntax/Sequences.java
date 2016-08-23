package org.panda_lang.panda.composition.syntax;

import org.panda_lang.core.interpreter.lexer.suggestion.Sequence;

public class Sequences {

    public static final Sequence STRING = new Sequence("String", '"');

    public static final Sequence CHARACTER = new Sequence("Character", "'");

    public static final Sequence LINE_ORIENTED_COMMENT = new Sequence("Comment", "//", System.lineSeparator());

    public static final Sequence BLOCK_ORIENTED_COMMENT = new Sequence("Comment", "/*", "*/");

    public static final Sequence DOCUMENTATION_ORIENTED_COMMENT = new Sequence("Comment", "/**", "*/");

    private static final Sequence[] VALUES = new Sequence[5];

    static {
        VALUES[0] = STRING;
        VALUES[1] = CHARACTER;
        VALUES[2] = LINE_ORIENTED_COMMENT;
        VALUES[3] = BLOCK_ORIENTED_COMMENT;
        VALUES[4] = DOCUMENTATION_ORIENTED_COMMENT;
    }

    public static Sequence[] values() {
        return VALUES;
    }

}
