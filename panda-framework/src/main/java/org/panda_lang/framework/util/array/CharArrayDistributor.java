package org.panda_lang.framework.util.array;

public class CharArrayDistributor {

    private final char[] array;
    private int index;

    public CharArrayDistributor(char[] array) {
        this.array = array;
        this.index = -1;
    }

    public char previous() {
        if (index - 1 < array.length) {
            index--;

            if (index < 0) {
                index = 0;
            }

            return array[index];
        }
        else {
            return getLast();
        }
    }

    public char current() {
        return array[index];
    }

    public boolean hasNext() {
        return index < array.length - 1;
    }

    public char next() {
        if (index + 1 < array.length) {
            index++;
            return array[index];
        }
        else {
            return getLast();
        }
    }

    public char further() {
        if (index + 1 < array.length) {
            return array[index + 1];
        }
        else {
            return getLast();
        }
    }

    public char future() {
        if (index + 2 < array.length) {
            return array[index + 2];
        }
        else {
            return getLast();
        }
    }

    public char getPrevious(int t) {
        return index - t > 0 ? array[index - t] : array[0];
    }

    public char getPrevious() {
        return index - 1 > 0 ? array[index - 1] : array[0];
    }

    public char getLast() {
        return array[array.length - 1];
    }

}
