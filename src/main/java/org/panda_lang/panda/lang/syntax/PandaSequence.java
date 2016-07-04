package org.panda_lang.panda.lang.syntax;

import org.panda_lang.panda.core.element.Sequence;

public enum PandaSequence {

    DOUBLE_QUOTES("String", '"', false),
    SINGLE_QUOTES("String", '\'', false),

    LINE_ORIENTED("Comment", "//", System.lineSeparator(), true),
    BLOCK_ORIENTED("Comment", "/*", "*/", true);

    private final Sequence sequence;

    PandaSequence(String literalName, char literal, boolean overlooked) {
        this(literalName, Character.toString(literal), overlooked);
    }

    PandaSequence(String literalName, String literal, boolean overlooked) {
        this(literalName, literal, literal, overlooked);
    }

    PandaSequence(String literalName, String literalStart, String literalEnd, boolean overlooked) {
        this(literalName, new String[]{ literalStart, literalEnd }, overlooked);
    }

    PandaSequence(String literalName, String[] literal, boolean overlooked) {
        this.sequence = new Sequence(literal).name(literalName).overlooked(overlooked);
    }

    public Sequence getSequence() {
        return sequence;
    }

}
