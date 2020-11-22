package org.panda_lang.utilities.commons.function.mutable;

public class Mutable<V> {

    private V value;

    public Mutable(V value) {
        this.value = value;
    }

    public V set(V updated) {
        V previous = value;
        this.value = updated;
        return previous;
    }

    public V get() {
        return value;
    }

}
