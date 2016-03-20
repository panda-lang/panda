package org.panda_lang.panda;

public class AnotherTest {

    public static void main(String[] args) {
        long initTime = System.nanoTime();
        long times = 100000000;

        long result = 0;
        for (int i = 0; i < times; i++) {
            String s = Integer.toString(i);
            result += s.length();
        }

        times = times - result;
        benchmark(initTime, times + result);
    }

    public static void benchmark(long initTime, long times) {
        long nsTime = System.nanoTime() - initTime;
        double msTime = nsTime / 1000000.0;
        double secTime = msTime / 1000.0;

        long averageTime = nsTime / times;
        double averageMsTime = averageTime / 1000000.0;
        double averageSecTime = averageMsTime / 1000.0;

        System.out.println("Benchmark:");
        System.out.println("    Full: " + nsTime + "ns -> " + msTime + "ms -> " + secTime + " seconds");
        System.out.println("    Average: " + averageTime + "ns -> " + averageMsTime + "ms -> " + averageSecTime + " seconds");
    }

}