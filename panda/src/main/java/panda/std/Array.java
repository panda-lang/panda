package panda.std;

import panda.utilities.ArrayUtils;

public final class Array<T> {

    private final T[] array;

    public Array(T[] array) {
        this.array = array;
    }

    public Option<T> get(int index) {
        return ArrayUtils.get(array, index);
    }

    public void set(int index, T value) {
        array[index] = value;
    }

    public T[] getJavaArray() {
        return array;
    }

}
