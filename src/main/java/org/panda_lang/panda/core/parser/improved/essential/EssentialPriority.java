package org.panda_lang.panda.core.parser.improved.essential;

public enum EssentialPriority {

    VIAL(1),
    VARIABLE(2),
    METHOD(3);

    private final double priority;

    private EssentialPriority(double priority) {
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }

}
