package org.panda_lang.core;

import org.panda_lang.core.util.BenchmarkUtils;

import java.util.ArrayList;

public class ArrayListTest {

    private static final int SIZE = 10_000_000;

    public static void main(String[] arguments) {
        long start = System.nanoTime();

        ArrayList<Object[]> arrayList = new ArrayList<>(SIZE);
        BenchmarkUtils.print(start, 1, "Create");

        start = System.nanoTime();
        for (int i = 0; i < SIZE; ++i) {
            arrayList.add(i, new Object[0]);
        }
        BenchmarkUtils.print(start, SIZE, "Add");

        start = System.nanoTime();
        arrayList.add(new Object[0]);
        BenchmarkUtils.print(start, 1, "Resize, aft:" + arrayList);
    }

}
