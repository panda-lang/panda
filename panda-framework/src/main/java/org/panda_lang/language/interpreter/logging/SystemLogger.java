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

package org.panda_lang.language.interpreter.logging;

import org.panda_lang.utilities.commons.StackTraceUtils;
import org.panda_lang.utilities.commons.console.Effect;

public final class SystemLogger implements Logger {

    private final Channel threshold;

    public SystemLogger() {
        this(Channel.INFO);
    }

    public SystemLogger(Channel threshold) {
        this.threshold = threshold;
    }

    @Override
    public void log(Channel channel, String message) {
        if (channel.getPriority() >= threshold.getPriority()) {
            System.out.println(Effect.paint(message));
        }
    }

    @Override
    public void error(String message) {
        log(Channel.ERROR, "# " + message);
    }

    @Override
    public void exception(Throwable throwable) {
        StackTraceElement[] stackTrace = StackTraceUtils.startsWith(throwable.getStackTrace(), element -> element.toString().contains("org.junit"));

        error("");
        error("&b- - ~ ~< Exception >~ ~ - -&r");
        error("");
        error("Given:");
        error("    Message:&1 " + throwable.getMessage());
        error("    In:&1 " + stackTrace[0].toString());
        error("    By:&1 " + throwable.getClass());
        error("");
        error("Stacktrace:");

        for (StackTraceElement element : stackTrace) {
            error("    at " + element.toString());
        }

        error("");
        error("Environment:");
        error("");

        throwable.printStackTrace(System.err);
    }

}
