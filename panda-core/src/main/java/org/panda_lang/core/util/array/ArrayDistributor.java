package org.panda_lang.core.util.array;

public class ArrayDistributor<T> {

    private final T[] array;
    private int index;

    public ArrayDistributor(T[] array) {
        this.array = array;
        this.index = -1;
    }

    public void reset() {
        this.index = -1;
    }

    public T previous() {
        if (index - 1 < array.length) {
            index--;

            if (index < 0) {
                index = 0;
            }

            return array[index];
        }

        return null;
    }

    public T current() {
        return index < array.length ? array[index] : null;
    }

    public boolean hasNext() {
        return index < array.length - 1;
    }

    public T next() {
        if (index + 1 < array.length) {
            index++;
            return array[index];
        }

        return null;
    }

    public T further() {
        if (index + 1 < array.length) {
            return array[index + 1];
        }

        return null;
    }

    public T future() {
        if (index + 2 < array.length) {
            return array[index + 2];
        }

        return null;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getPrevious() {
        return index - 1 > 0 ? array[index - 1] : null;
    }

    public T getPrevious(int t) {
        return index - t > 0 ? array[index - t] : null;
    }

    public T getLast() {
        return array[array.length - 1];
    }

    public int getIndex() {
        return index;
    }

    public int getLength() {
        return array.length;
    }

}
