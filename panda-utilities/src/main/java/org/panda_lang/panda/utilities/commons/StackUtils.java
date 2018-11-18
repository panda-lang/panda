package org.panda_lang.panda.utilities.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackUtils {

    public static <T> Stack<T> popSilently(Stack<T> stack, int amount) {
        for (int i = 0; i < amount; i++) {
            if (!stack.isEmpty()) {
                stack.pop();
            }
        }

        return stack;
    }

    public static <T> Stack<T> fill(Stack<T> stack, T object, int amount) {
        for (int i = 0; i < amount; i++) {
            stack.push(object);
        }

        return stack;
    }

    public static <T> Stack<T> reverse(Stack<T> stack) {
        List<T> reversed = new ArrayList<>(stack);

        Collections.reverse(reversed);
        stack.clear();

        for (T element : reversed) {
            stack.push(element);
        }

        return stack;
    }

}
