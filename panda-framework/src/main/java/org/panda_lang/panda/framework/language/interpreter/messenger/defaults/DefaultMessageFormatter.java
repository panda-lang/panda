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

import org.panda_lang.panda.framework.PandaFrameworkConstants;
import org.panda_lang.panda.utilities.commons.text.MessageFormatter;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultMessageFormatter {

    private static final Map<String, String> ENVIRONMENT = new LinkedHashMap<>();

    private static final MessageFormatter formatter = new MessageFormatter()
            .register("{{environment}}", () -> {
                StringBuilder content = new StringBuilder();

                ENVIRONMENT.forEach((key, value) -> content
                        .append("{{newline}}  ")
                        .append(key)
                        .append(": ")
                        .append(value)
                );

                return content.toString();
            })
            .register("{{newline}}", System::lineSeparator);

    static {
        ENVIRONMENT.put("Panda", PandaFrameworkConstants.VERSION);
        ENVIRONMENT.put("Java", System.getProperty("java.version"));
        ENVIRONMENT.put("OS",  System.getProperty("os.name"));
    }

    public static void addEnvironmentInfo(String title, String content) {
        ENVIRONMENT.put(title, content);
    }

    public static MessageFormatter getOriginalFormatter() {
        return formatter;
    }

    public static MessageFormatter getFormatter() {
        return formatter.fork();
    }

}
