package org.panda_lang.panda.util;

import java.lang.reflect.Array;
import java.util.Stack;

public class StackUtils {

    public static <T> T[] toArray(Class<T> clazz, Stack<T> stack) {
        return stack.toArray((T[]) Array.newInstance(clazz, stack.size()));
    }

}
