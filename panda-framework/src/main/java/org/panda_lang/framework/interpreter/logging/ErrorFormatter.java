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

package org.panda_lang.framework.interpreter.logging;

import org.panda_lang.framework.Failure;
import org.panda_lang.framework.PandaFrameworkConstants;
import org.panda_lang.framework.architecture.statement.Statement;
import org.panda_lang.framework.interpreter.source.IndicatedSource;
import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.framework.runtime.PandaProcessFailure;
import panda.utilities.StackTraceUtils;
import panda.utilities.StringUtils;
import panda.utilities.console.Colored;
import panda.utilities.console.Effect;
import panda.std.Option;

public final class ErrorFormatter {

    private final Logger logger;

    public ErrorFormatter(Logger logger) {
        this.logger = logger;
    }

    public void print(Throwable throwable) {
        StackTraceElement[] stackTrace = cleanStackTrace(throwable);

        if (throwable instanceof Failure) {
            Failure failure = (Failure) throwable;

            logger.error("");
            logger.error("&b- - ~ ~< Failure >~ ~ - -&r");
            logger.error("");
            logger.error("&1" + failure.getMessage());
            logger.error("");

            failure.getNote()
                    .filterNot(StringUtils::isEmpty)
                    .peek(note -> {
                        logger.error("Note:");
                        logger.error("  &1" + note);
                        logger.error("");
                    });

            IndicatedSource indicatedSource = failure.getIndicatedSource();
            Location location = indicatedSource.getIndicated().getLocation();

            String source = getCurrentLine(indicatedSource.getSource(), indicatedSource.getIndicated()).toString();
            String element = getCurrentLine(indicatedSource.getIndicated(), indicatedSource.getIndicated()).toString();

            int elementIndex = source.indexOf(" " + element + " ");
            elementIndex += elementIndex < 0 ? 0 : 1;
            int endIndex = elementIndex + element.length();

            String content = elementIndex < 0 ? source : source.substring(0, elementIndex)
                    + Colored.on(source.substring(elementIndex, endIndex)).effect(Effect.RED)
                    + source.substring(endIndex);

            logger.error("Source:");
            logger.error("  " + content);
            logger.error("  " + StringUtils.buildSpace(elementIndex) + Colored.on("^").effect(Effect.BOLD));
            logger.error("Location:");
            logger.error("  Panda: &b" + location.getSource().getId() + "&r at line &1" + location.getDisplayLine() + "&r:&1" + location.getIndex());
            logger.error("");
            logger.error("Stacktrace:");
            printStackTrace(throwable);
        }
        else if (throwable instanceof PandaProcessFailure) {
            PandaProcessFailure failure = (PandaProcessFailure) throwable;
            stackTrace = cleanStackTrace(failure.getException());

            logger.error("");
            logger.error("&b- - ~ ~< Runtime Error >~ ~ - -&r");
            logger.error("");
            logger.error("&1" + throwable.getMessage() + "&r");

            for (Statement statement : failure.getStack().getLivingFramesOnStack()) {
                Location location = statement.getSourceLocation();
                logger.error("  at " + location.getSource().getId() + " [&1" + location.getDisplayLine() + "&r:&1" + location.getIndex() + "&r]");
            }

            logger.error("");
            logger.error("Stack:");

            for (int index = 0; index < 5 && index < stackTrace.length; index++) {
                StackTraceElement stackTraceElement = stackTrace[index];
                logger.error("  at " + stackTraceElement.toString());
            }
        }
        else {
            logger.error("");
            logger.error("&b- - ~ ~< Error >~ ~ - -&r");
            logger.error("");
            logger.error("Given:");
            logger.error("  Message:&1 " + Option.of(throwable.getMessage()).orElseGet(throwable.toString()));
            logger.error("  In:&1 " + stackTrace[0].toString());
            logger.error("  By:&1 " + throwable.getClass());
            logger.error("");
            logger.error("Stacktrace:");

            for (StackTraceElement stackTraceElement : stackTrace) {
                logger.error("  at " + stackTraceElement);
            }
        }

        logger.error("");
        logger.error("Environment:");
        logger.error("  Panda: " + PandaFrameworkConstants.VERSION);
        logger.error("  Java: " + System.getProperty("java.version") + " (" + System.getProperty("os.name") + ")");
        logger.error("");
    }

    private void printStackTrace(Throwable throwable) {
        StackTraceElement[] stackTrace = cleanStackTrace(throwable);

        for (int index = 0; index < 2 && index < stackTrace.length; index++) {
            StackTraceElement stackTraceElement = stackTrace[index];
            logger.error("  at " + stackTraceElement.toString());
        }

        if (throwable.getCause() != null) {
            logger.error("  --- cause: " + throwable.getCause().toString());
            printStackTrace(throwable.getCause());
        }
    }

    private StackTraceElement[] cleanStackTrace(Throwable throwable) {
        StackTraceElement[] stackTrace = StackTraceUtils.filter(throwable.getStackTrace(), "sun.reflect", "java.lang.reflect.");
        return StackTraceUtils.startsWith(stackTrace, element -> element.toString().contains("org.junit"));
    }

    private Snippet getCurrentLine(Snippetable source, Snippetable indicated) {
        return source.toSnippet().getLine(indicated.toSnippet().getFirst().getLocation().getLine());
    }

}
