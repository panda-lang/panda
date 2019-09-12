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

package org.panda_lang.panda.language.interpreter.messenger.template;

import org.panda_lang.panda.utilities.commons.CharacterUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;
import org.panda_lang.panda.utilities.commons.console.Effect;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MicroTemplateProcessor {

    private static final Pattern CONDITION = Pattern.compile("(?:%([^%]+)%)\\r?\\n((?:.+\\s*)*?)(?:% end %)\\r?\\n");

    protected String processContent(String content, Map<String, Object> context) {
        StringBuilder contentBuilder = new StringBuilder();

        Matcher matcher = CONDITION.matcher(content);
        int previousIndex = 0;

        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String value = matcher.group(2);

            contentBuilder.append(content, previousIndex, matcher.start());
            previousIndex = matcher.end();

            if (context.get(condition) == null) {
                continue;
            }

            contentBuilder.append(value);
        }

        return contentBuilder
                .append(content.substring(previousIndex))
                .toString();
    }

    protected String insertPrefix(String content, String prefix) {
        String[] lines = StringUtils.split(content, System.lineSeparator());

        for (int i = 0; i < lines.length; i++) {
            lines[i] = CharacterUtils.BACKSPACE + prefix + lines[i];
        }

        return ContentJoiner.on(System.lineSeparator())
                .join(lines)
                .toString();
    }

    protected String colored(String content) {
        for (Effect effect : Effect.values()) {
            content = StringUtils.replace(content, "&" + effect.getSimpleCode(), effect.getCode());
        }

        return content;
    }

}
