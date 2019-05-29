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

package org.panda_lang.panda.framework.language.interpreter.messenger.defaults;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.PackageUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.console.Colored;
import org.panda_lang.panda.utilities.commons.console.Effect;
import org.panda_lang.panda.utilities.commons.text.MessageFormatter;

public class DefaultFailureTemplateBuilder {

    private String content = "{{newline}}" + Colored.on("- - ~ ~< Messenger :: Severe Failure >~ ~ - -").effect(Effect.BOLD) + "{{newline}}";

    public DefaultFailureTemplateBuilder applyPlaceholders(MessageFormatter formatter, InterpreterFailure exception) {
        String source = exception.getSource();
        String element = exception.getElement();

        int index = source.indexOf(element);
        int endIndex = index + element.length();

        formatter.register("{{line}}", () -> exception.getLine() < 0 ? "?" : exception.getLine() + 1)
                .register("{{position}}", () -> "~") // TODO: position
                .register("{{location}}", exception::getLocation)
                .register("{{message}}", exception::getMessage)
                .register("{{index}}", () -> index)
                .register("{{source}}", () -> index < 0 ? source : source.substring(0, index)
                    + Colored.on(source.substring(index, endIndex)).effect(Effect.RED)
                    + source.substring(endIndex))
                .register("{{stacktrace-last}}", () -> {
                    StackTraceElement lastElement = ArrayUtils.get(exception.getStackTrace(), 0);

                    if (lastElement == null) {
                        return "<unknown>";
                    }

                    return PackageUtils.getShortenPackage(lastElement.getClassName()) + " (" + lastElement.getFileName() + ":" + lastElement.getLineNumber() + ")";
                })
                .register("{{stacktrace}}", new StacktraceSupplier(exception));

        return this;
    }

    public DefaultFailureTemplateBuilder includeCause() {
        content += getAsSection("Caused by: {{message}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeSourceDetails(Object details) {
        if (details == null) {
            return this;
        }

        content += getAsSection("Details:{{newline}}  {{details}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeSource() {
        content += getAsSection("Source:{{newline}}  {{source}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeLocation() {
        content += getAsSection("Location:{{newline}}  Panda: {{location}} at line {{line}}:{{position}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeSourceDetails() {
        content += getAsSection("Details:{{newline}}" +
                "  Generation: {{generation}}{{newline}}" +
                // "  Framework: {{stacktrace-last}}{{newline}}" +
                "  Stack: {{stacktrace}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeMarker(int index) {
        if (index < 0) {
            return this;
        }

        content += "  " + StringUtils.buildSpace(index) + "^{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeEnvironment() {
        content += getAsSection("Environment:{{environment}}");
        return this;
    }

    public DefaultFailureTemplateBuilder includeEnd() {
        content += getAsSection("End of Failure");
        return this;
    }

    private String getAsSection(String content) {
        return "{{newline}}" + content + "{{newline}}";
    }

    public String[] getAsLines(MessageFormatter formatter, String title) {
        String formattedContent = formatter.format(content);
        String[] lines = StringUtils.split(formattedContent, System.lineSeparator());

        for (int i = 0; i < lines.length; i++) {
            lines[i] = "[" + title + "] #!# " + lines[i];
        }

        return lines;
    }

    public String getContent() {
        return content;
    }

    public static @Nullable String indentation(String message) {
        return message == null ? null : StringUtils.replace(message, System.lineSeparator(), System.lineSeparator() + "  ");
    }

    public static String stacktraceToString(Throwable exception) {
        StringBuilder message = new StringBuilder();

        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            message.append(stackTraceElement);
            message.append(System.lineSeparator());
        }

        return DefaultFailureTemplateBuilder.indentation(message.toString());
    }

}
