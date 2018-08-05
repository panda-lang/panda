/*
 * Copyright (c) 2015-2018 Dzikoysk
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

import org.fusesource.jansi.Ansi;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.InterpreterFailure;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;
import org.panda_lang.panda.utilities.commons.redact.MessageFormatter;

public class DefaultFailureTemplateBuilder {

    private String content = Ansi.ansi().a("{{newline}}").bold().a("- - ~ ~< Messenger :: Severe Failure >~ ~ - -").reset().a("{{newline}}").toString();

    public DefaultFailureTemplateBuilder applyPlaceholders(MessageFormatter formatter, InterpreterFailure exception) {
        String source = exception.getSource();
        String element = exception.getElement();

        int index = source.indexOf(element);
        int endIndex = index + element.length();

        formatter.register("{{line}}", () -> exception.getLine() < 0 ? "?" : exception.getLine() + 1)
                .register("{{location}}", exception::getLocation)
                .register("{{message}}", exception::getMessage)
                .register("{{index}}", () -> index)
                .register("{{source}}", () -> index < 0 ? source : Ansi.ansi()
                        .a(source.substring(0, index))
                        .fgRed()
                        .a(source.substring(index, endIndex))
                        .reset()
                        .a(source.substring(endIndex, source.length()))
                        .toString())
                .register("{{stacktrace}}", () -> {
                    StringBuilder message = new StringBuilder();

                    for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                        message.append(stackTraceElement);
                        message.append(System.lineSeparator());
                    }

                    return DefaultFailureTemplateBuilder.indentation(message.toString());
                });

        return this;
    }

    public DefaultFailureTemplateBuilder includeCause() {
        content += "{{newline}}Caused by: {{message}} [in {{location}} at line {{line}}]{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeDetails(Object details) {
        if (details == null) {
            return this;
        }

        content += "{{newline}}Details:{{newline}}  {{details}}{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeSource() {
        content += "{{newline}}Source:{{newline}}  {{source}}{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeMarker(int index) {
        if (index < 0) {
            return this;
        }

        content += "  " + StringUtils.createIndentation(index) + "^{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeEnvironment() {
        content += "{{newline}}Environment:{{newline}}  OS: {{os}}{{newline}}  Panda: {{panda.version}}{{newline}}  Java: {{java.version}}{{newline}}";
        return this;
    }

    public DefaultFailureTemplateBuilder includeEnd() {
        content += "{{newline}}End of Failure {{newline}} ";
        return this;
    }

    public String[] getAsLines(MessageFormatter formatter, String title) {
        String formattedContent = formatter.format(content);
        String[] lines = formattedContent.split(System.lineSeparator());

        for (int i = 0; i < lines.length; i++) {
            lines[i] = "[" + title + "] #!# " + lines[i];
        }

        return lines;
    }

    public String getContent() {
        return content;
    }

    public static @Nullable String indentation(String message) {
        return message == null ? null : message.replace(System.lineSeparator(), System.lineSeparator() + "  ");
    }

}
