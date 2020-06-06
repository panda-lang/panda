/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.util;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.messenger.LoggerHolder;
import org.panda_lang.utilities.commons.TimeUtils;
import org.panda_lang.utilities.commons.UnsafeUtils;

import java.util.Map;

public final class PandaUtils {

    private PandaUtils() { }

    public static <T> @Nullable T eval(Map<String, Object> context, String expression) {
        return null;
    }

    /**
     * Print current JVM startup time and disable illegal access message.
     * The method should be called as fast as it is possible.
     */
    public static void printJVMUptime(LoggerHolder loggerHolder) {
        UnsafeUtils.disableIllegalAccessMessage();

        loggerHolder.getLogger().debug("");
        loggerHolder.getLogger().debug("JVM launch time: " + TimeUtils.getJVMUptime() + "ms (｡•́︿•̀｡)");
        loggerHolder.getLogger().debug("");
    }

}
