package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Stack;

class StackUtilsTest {

    @Test
    public void testFill() {
        Assertions.assertEquals(9, StackUtils.fill(new Stack<>(), new Object(), 9).size());
    }

    @Test
    public void testPopSilently() {
        Stack<?> stack = StackUtils.fill(new Stack<>(), new Object(), 10);
        Assertions.assertEquals(5, StackUtils.popSilently(stack, 5).size());
        Assertions.assertEquals(0, StackUtils.popSilently(stack, 6).size());
    }

}
