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

package org.panda_lang.panda.language.interpreter.messenger.formatters;

import org.panda_lang.language.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.language.interpreter.messenger.MessengerDataFormatter;
import org.panda_lang.panda.language.interpreter.parser.ApplicationParser;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.PackageUtils;
import org.panda_lang.utilities.commons.StringUtils;

import org.panda_lang.utilities.commons.function.Option;

public final class StacktraceElementsFormatter implements MessengerDataFormatter<StackTraceElement[]> {

    @Override
    public void onInitialize(MessengerTypeFormatter<StackTraceElement[]> typeFormatter) {
        typeFormatter
                .register("{{stacktrace-elements.default}}", (messengerFormatter, stackTraceElements) -> {
                    StringBuilder stacktrace = new StringBuilder();

                    for (StackTraceElement element : stackTraceElements) {
                        stacktrace.append(System.lineSeparator())
                                .append(StringUtils.buildSpace(2))
                                .append(element.toString());
                    }

                    return stacktrace.toString();
                })
                .register("{{stacktrace-elements.last}}", (messengerFormatter, stackTraceElements) -> {
                    Option<StackTraceElement> lastElement = ArrayUtils.get(stackTraceElements, 0);

                    if (!lastElement.isPresent()) {
                        return "<unknown>";
                    }

                    StackTraceElement element = lastElement.get();
                    return PackageUtils.getShortenPackage(element.getClassName()) + " (" + element.getFileName() + ":" + element.getLineNumber() + ")";
                })
                .register("{{stacktrace-elements.trimmed}}", (messengerFormatter, stackTraceElements) -> {
                    StringBuilder stacktrace = new StringBuilder();

                    for (StackTraceElement element : stackTraceElements) {
                        boolean status = ClassUtils.forName(element.getClassName())
                                .map(clazz -> clazz == ApplicationParser.class)
                                .orElseGet(false);

                        stacktrace.append(System.lineSeparator())
                                .append(StringUtils.buildSpace(2))
                                .append(element.toString());

                        if (status) {
                            break;
                        }
                    }

                    return stacktrace.toString();
                });
    }

    @Override
    public Class<StackTraceElement[]> getType() {
        return StackTraceElement[].class;
    }

}
