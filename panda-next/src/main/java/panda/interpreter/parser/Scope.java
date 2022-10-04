package panda.interpreter.parser;

public enum Scope implements Comparable<Scope> {

    SCRIPT(1.0),
    TYPE(2.0),
    SIGNATURE(3.0),
    BODY(3.0);

    /** Lower means it's less important */
    private final double priority;

    Scope(double priority) {
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }

}
