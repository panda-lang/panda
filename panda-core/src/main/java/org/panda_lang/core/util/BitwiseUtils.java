package org.panda_lang.core.util;

public class BitwiseUtils {

    /**
     * Convert 2 ints to 1 long
     */
    public static long convert(int a, int b) {
        return a << 32 | b & 0xFFFFFFFFL;
    }

    /**
     * Extract 1st int from long
     */
    public static int extractLeft(long l) {
        return (int) (l >> 32);
    }

    /**
     * Extract 2nd int from long
     */
    public static int extractRight(long l) {
        int a = (int) (l >> 32);
        return (int) l;
    }

    /**
     * Extract 2 ints from long
     */
    public static int[] extract(long l) {
        return new int[]{ (int) l >> 32, (int) l };
    }

}
