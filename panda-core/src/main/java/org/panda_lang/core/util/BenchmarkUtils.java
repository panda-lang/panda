package org.panda_lang.core.util;

public class BenchmarkUtils {

    public static void print(long start, int av, String message) {
        long nsDif = System.nanoTime() - start;
        float msDif = nsDif / 1000000;
        float sDif = msDif / 1000;
        float mDif = sDif / 60;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ns: ");
        stringBuilder.append(nsDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(nsDif / av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ms: ");
        stringBuilder.append(msDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(msDif / av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    s: ");
        stringBuilder.append(sDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(sDif / av);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    m: ");
        stringBuilder.append(mDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(mDif / av);
        stringBuilder.append(System.lineSeparator());

        System.out.println(stringBuilder.toString());
    }

}
