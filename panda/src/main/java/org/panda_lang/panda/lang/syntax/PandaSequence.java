package org.panda_lang.panda.lang.syntax;

import org.panda_lang.core.interpreter.parser.redact.Sequence;

public enum PandaSequence {

    DOUBLE_QUOTES("String", '"', false),
    SINGLE_QUOTES("String", '\'', false),

    LINE_ORIENTED("Comment", "//", System.lineSeparator(), true),
    BLOCK_ORIENTED("Comment", "/*", "*/", true);

    private final Sequence sequence;

    PandaSequence(String literalName, char literal, boolean negligible) {
        this(literalName, Character.toString(literal), negligible);
    }

    PandaSequence(String literalName, String literal, boolean negligible) {
        this(literalName, literal, literal, negligible);
    }

    PandaSequence(String literalName, String literalStart, String literalEnd, boolean negligible) {
        this(literalName, new String[]{ literalStart, literalEnd }, negligible);
    }

    PandaSequence(String literalName, String[] literal, boolean negligible) {
        this.sequence = new Sequence(literal).name(literalName).negligible(negligible);
    }

    public Sequence getSequence() {
        return sequence;
    }

}
