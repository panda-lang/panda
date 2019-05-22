/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.utilities.commons;

import org.panda_lang.panda.utilities.commons.function.ThrowingRunnable;
import org.panda_lang.panda.utilities.commons.function.ThrowingSupplier;

public class BenchmarkUtils {

    /**
     * Execute the runnable with the specified title and print an execution time in the console
     *
     * @param title    the title of test
     * @param runnable the runnable to execute
     */
    public static void execute(String title, ThrowingRunnable runnable) {
        long time = System.nanoTime();

        try {
            runnable.run();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        System.out.println("[" + title + "] " + TimeUtils.toMilliseconds(System.nanoTime() - time));
    }

    /**
     * Execute the supplier with the specified title, print an execution time in the console and return resulting value
     *
     * @param title    the title of test
     * @param supplier the supplier to execute
     * @param <T>      generic type of the value to return
     * @return the resulting value
     */
    public static <T> T execute(String title, ThrowingSupplier<T> supplier) {
        long time = System.nanoTime();

        T value;

        try {
            value = supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        System.out.println("[" + title + "] " + TimeUtils.toMilliseconds(System.nanoTime() - time));
        return value;
    }

    /**
     * Display the message with the specified time in different formats
     *
     * @param start    the period of time
     * @param attempts the amount of attempts required to calculate an average time
     * @param message  the message of test
     */
    public static void print(long start, int attempts, String message) {
        long nsDif = System.nanoTime() - start;
        float msDif = nsDif / 1000000F;
        float sDif = msDif / 1000;
        float mDif = sDif / 60;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ns: ");
        stringBuilder.append(nsDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(nsDif / attempts);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    ms: ");
        stringBuilder.append(msDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(msDif / attempts);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    s: ");
        stringBuilder.append(sDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(sDif / attempts);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("    m: ");
        stringBuilder.append(mDif);
        stringBuilder.append(" | av: ");
        stringBuilder.append(mDif / attempts);
        stringBuilder.append(System.lineSeparator());

        System.out.println(stringBuilder);
    }

}
