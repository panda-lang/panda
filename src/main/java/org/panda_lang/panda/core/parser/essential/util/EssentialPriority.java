package org.panda_lang.panda.core.parser.essential.util;

public enum EssentialPriority {

    VIAL(1),
    VARIABLE(2),
    METHOD(3);

    private final double priority;

    EssentialPriority(double priority) {
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }

}
