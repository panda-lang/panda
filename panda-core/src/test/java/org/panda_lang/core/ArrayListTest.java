package org.panda_lang.core;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

public class ArrayListTest {

    private static final int SIZE = 10_000_000;

    public static void main(String[] arguments) {
        long start = System.nanoTime();

        ArrayList<Object[]> arrayList = new ArrayList<>(SIZE);
        print(start, 1, "Create");

        start = System.nanoTime();
        for (int i = 0; i < SIZE; ++i) {
            arrayList.add(i, new Object[0]);
        }
        print(start, SIZE, "Add");

        start = System.nanoTime();
        arrayList.add(new Object[0]);
        print(start, 1, "Resize, aft:" + arrayList);
    }

    public static void print(long start, int av, String message) {
        long nsDif = System.nanoTime() - start;
        float msDif = nsDif/1000000;
        float sDif = msDif/1000;
        float mDif = sDif/60;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ns: ");
        stringBuilder.append(nsDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(nsDif/av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ms: ");
        stringBuilder.append(msDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(msDif/av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    s: ");
        stringBuilder.append(sDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(sDif/av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    m: ");
        stringBuilder.append(mDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(mDif/av);
        stringBuilder.append(System.lineSeparator());

        System.out.println(stringBuilder.toString());
    }

}
