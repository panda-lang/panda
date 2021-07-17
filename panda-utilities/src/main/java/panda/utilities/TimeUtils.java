/*
 * Copyright (c) 2021 dzikoysk
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

package panda.utilities;

import java.lang.management.ManagementFactory;
import java.util.Locale;

public final class TimeUtils {

    public static final double MILLISECOND = 1_000;
    public static final double NANOSECOND = 1_000_000;

    private TimeUtils() { }

    /**
     * Format long representing time in milliseconds to formatted time in seconds
     *
     * @param ms the time to format
     * @return the formatted time
     */
    public static String toSeconds(long ms) {
        return format(ms / MILLISECOND) + "s";
    }

    /**
     * Format long representing time in nanoseconds to formatted time in milliseconds
     *
     * @param nano the time to format
     * @return the formatted time
     */
    public static String toMilliseconds(long nano) {
        return format(nano / NANOSECOND) + "ms";
    }

    /**
     * Format double representing time to two decimal places
     *
     * @param time the time to format
     * @return the formatted time
     */
    public static String format(double time) {
        return String.format(Locale.US, "%.2f", time);
    }

    /**
     * Calculate uptime since the given time in milliseconds
     *
     * @param uptime the init time
     * @return the difference between init time and current time in milliseconds
     */
    public static double getUptime(long uptime) {
        return (System.currentTimeMillis() - uptime) / MILLISECOND;
    }

    /**
     * Get estimated time of JVM boot time
     *
     * @return the estimated boot time
     */
    public static long getJVMUptime() {
        return System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
    }

}
