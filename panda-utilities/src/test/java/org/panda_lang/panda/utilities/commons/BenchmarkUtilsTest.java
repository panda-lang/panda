package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

class BenchmarkUtilsTest {

    @Test
    public void testExecuteRunnable() {
        AtomicBoolean test = new AtomicBoolean(false);
        BenchmarkUtils.execute("Test::Runnable", () -> test.set(true));
        Assertions.assertTrue(test.get());
    }

    @Test
    public void testExecuteSupplier() {
        Assertions.assertTrue(BenchmarkUtils.execute("Test::Supplier", () -> true));
    }

}
