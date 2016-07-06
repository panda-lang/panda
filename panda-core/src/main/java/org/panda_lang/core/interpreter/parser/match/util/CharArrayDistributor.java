package org.panda_lang.core.interpreter.parser.match.util;

public class CharArrayDistributor {

    private final char[] array;
    private int i;

    public CharArrayDistributor(char[] array) {
        this.array = array;
    }

    public char previous() {
        if (i - 1 < array.length) {
            i--;

            if (i < 0) {
                i = 0;
            }

            return array[i];
        }
        else {
            return getLast();
        }
    }

    public char current() {
        return array[i];
    }

    public boolean hasNext() {
        return i < array.length - 1;
    }

    public char next() {
        if (i + 1 < array.length) {
            i++;
            return array[i];
        }
        else {
            return getLast();
        }
    }

    public char further() {
        if (i + 1 < array.length) {
            return array[i + 1];
        }
        else {
            return getLast();
        }
    }

    public char future() {
        if (i + 2 < array.length) {
            return array[i + 2];
        }
        else {
            return getLast();
        }
    }

    public char getPrevious() {
        return i - 1 > 0 ? array[i - 1] : array[0];
    }

    public char getPrevious(int t) {
        return i - t > 0 ? array[i - t] : array[0];
    }

    public char getLast() {
        return array[array.length - 1];
    }

}
