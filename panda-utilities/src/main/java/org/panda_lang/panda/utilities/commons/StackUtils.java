package org.panda_lang.panda.utilities.commons;

import java.util.Stack;

public class StackUtils {

    public static void popSilently(Stack<?> stack, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }
    }

}
