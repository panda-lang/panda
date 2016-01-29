package org.panda_lang.panda.core.parser.essential.util;

public enum EssentialPriority {

    VIAL(1),
    VARIABLE(2),
    METHOD(3),
    GROUP(4),
    IMPORT(5),
    RETURN(6),
    CONTINUE(7);

    private final int priority;

    EssentialPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
