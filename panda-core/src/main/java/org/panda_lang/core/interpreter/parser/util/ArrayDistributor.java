package org.panda_lang.core.interpreter.parser.util;

public class ArrayDistributor<T> {

    private final T[] array;
    private int i;

    public ArrayDistributor(T[] array) {
        this.array = array;
    }

    public T previous() {
        if (i - 1 < array.length) {
            i--;

            if (i < 0) {
                i = 0;
            }

            return array[i];
        }

        return null;
    }

    public T current() {
        return array[i];
    }

    public boolean hasNext() {
        return i < array.length - 1;
    }

    public T next() {
        if (i + 1 < array.length) {
            i++;
            return array[i];
        }

        return null;
    }

    public T further() {
        if (i + 1 < array.length) {
            return array[i + 1];
        }

        return null;
    }

    public T future() {
        if (i + 2 < array.length) {
            return array[i + 2];
        }

        return null;
    }

    public T getPrevious() {
        return i - 1 > 0 ? array[i - 1] : null;
    }

    public T getPrevious(int t) {
        return i - t > 0 ? array[i - t] : null;
    }

    public T getLast() {
        return array[array.length - 1];
    }

}
