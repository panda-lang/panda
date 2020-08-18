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

import org.panda_lang.language.Failure;
import org.panda_lang.language.PandaFrameworkConstants;
import org.panda_lang.language.interpreter.source.IndicatedSource;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.StackTraceUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.console.Colored;
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

        if (throwable instanceof Failure) {
            Failure failure = (Failure) throwable;

            error("");
            error("&b- - ~ ~< Failure >~ ~ - -&r");
            error("");
            error("&1" + failure.getMessage());
            error("");

            failure.getNote().peek(note -> {
                error("Note:");
                error("  &1" + note);
                error("");
            });

            IndicatedSource indicatedSource = failure.getIndicatedSource();
            Location location = indicatedSource.getIndicated().getLocation();

            String source = getCurrentLine(indicatedSource.getSource(), indicatedSource.getIndicated()).toString();
            String element = getCurrentLine(indicatedSource.getIndicated(), indicatedSource.getIndicated()).toString();

            int elementIndex = source.indexOf(element);
            int endIndex = elementIndex + element.length();

            String content = elementIndex < 0 ? source : source.substring(0, elementIndex)
                    + Colored.on(source.substring(elementIndex, endIndex)).effect(Effect.RED)
                    + source.substring(endIndex);


            error("Source:");
            error("  " + content);
            error("  " + StringUtils.buildSpace(source.indexOf(element)) + Colored.on("^").effect(Effect.BOLD));
            error("Location:");
            error("  Panda: &b" + location.getSource().getId() + "&r at line &1" + location.getDisplayLine() + "&r:&1" + location.getIndex());
            error("");
            error("Stacktrace:");

            for (int index = 0; index < 2 && index < stackTrace.length; index++) {
                StackTraceElement stackTraceElement = stackTrace[index];
                error("  at " + stackTraceElement.toString());
            }
        }
        else {
            error("");
            error("&b- - ~ ~< Exception >~ ~ - -&r");
            error("");
            error("Given:");
            error("  Message:&1 " + throwable.getMessage());
            error("  In:&1 " + stackTrace[0].toString());
            error("  By:&1 " + throwable.getClass());
            error("");
            error("Stacktrace:");

            for (StackTraceElement stackTraceElement : stackTrace) {
                error("  at " + stackTraceElement);
            }
        }

        error("");
        error("Environment:");
        error("  Panda: " + PandaFrameworkConstants.VERSION);
        error("  Java: " + System.getProperty("java.version") + " (" + System.getProperty("os.name") + ")");
        error("");
    }

    private Snippet getCurrentLine(Snippetable source, Snippetable indicated) {
        return source.toSnippet().getLine(indicated.toSnippet().getFirst().getLocation().getLine());
    }

}
