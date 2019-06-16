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

package org.panda_lang.panda.framework.language.interpreter.messenger.formatter;

import org.panda_lang.panda.framework.design.interpreter.Interpreter;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Objects;
import java.util.function.Supplier;

final class StacktraceSupplier implements Supplier<String> {

    private final Throwable exception;

    public StacktraceSupplier(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public String get() {
        StringBuilder content = new StringBuilder();
        StackTraceElement[] elements = exception.getStackTrace();

        String previousClass = null;
        int count = 0;
        int gap = 0;

        for (int i = 0; i < elements.length; i++) {
            StackTraceElement lastElement = ArrayUtils.get(exception.getStackTrace(), i);

            if (lastElement == null) {
                break;
            }

            if (i != 0 && (!lastElement.getClassName().startsWith("org.panda_lang")
                    || lastElement.getClassName().contains("ParserLayerGenerator")
                    || lastElement.getClassName().contains("UnifiedBootstrapParser")
                    || lastElement.getClassName().contains("UnifiedParserBootstrap")
                    || lastElement.getClassName().contains("Generation")
                    || lastElement.isNativeMethod())) {

                gap++;
                continue;
            }

            if (gap > 0) {
                content.append("(CI: ").append(gap).append(") <- ");
                gap = 0;
            }

            if (Objects.equals(lastElement.getFileName(), previousClass)) {
                continue;
            }

            Class<?> className = ReflectionUtils.forName(lastElement.getClassName());
            String fileName = className != null ? lastElement.getFileName() : lastElement.getClassName();

            previousClass = lastElement.getFileName();
            count++;

            content.append("(")
                    .append(fileName).append(":")
                    .append(lastElement.getLineNumber())
                    .append(") <- ");

            if (className == null || Interpreter.class.isAssignableFrom(className)) {
                break;
            }
        }

        if (gap > 0) {
            content.append("(CI: ").append(gap).append(") <- ");
        }

        return content.append("[...]").toString();
    }

}
