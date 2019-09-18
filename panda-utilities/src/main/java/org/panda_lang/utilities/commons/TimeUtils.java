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

package org.panda_lang.utilities.commons;

import java.lang.management.ManagementFactory;

public final class TimeUtils {

    public static final double MILLISECOND = 1_000;
    public static final double NANOSECOND = 1_000_000;

    private TimeUtils() { }

    public static String toSeconds(long ms) {
        return (ms / MILLISECOND) + "s";
    }

    public static String toMilliseconds(long nano) {
        return (nano / NANOSECOND) + "ms";
    }

    public static double getUptime(long uptime) {
        return (System.currentTimeMillis() - uptime) / MILLISECOND;
    }

    public static long getJVMUptime() {
        return System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime();
    }

}
