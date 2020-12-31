package org.panda_lang.utilities.commons.function.mutable;

public final class Counter {

    private int index;

    public Counter(int init) {
        this.index = init;
    }

    public Counter() {
        this(0);
    }

    public int get() {
        return index;
    }

    public int getAndIncrement() {
        return index++;
    }

    public int incrementAndGet() {
        return ++index;
    }

}
