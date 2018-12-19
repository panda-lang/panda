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

package org.panda_lang.panda.framework.language.interpreter.parser.general.number;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.utilities.commons.CharacterUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class NumberUtils {

    public static final char[] ALLOWED_CHARACTERS = new char[] { '.', '_', 'x' };

    public static final char[] NUMBER_EXTENSIONS = new char[]{ 'b', 'B', 's', 'S', 'i', 'I', 'l', 'L', 'd', 'D', 'f', 'F' };

    public static boolean isNumeric(Tokens source) {
        return isNumeric(source.asString());
    }

    public static boolean isNumeric(String content) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }

        boolean digit = false;

        for (char c : content.toCharArray()) {
            if (Character.isDigit(c)) {
                digit = true;
                continue;
            }

            if (CharacterUtils.belongsTo(c, ALLOWED_CHARACTERS, NUMBER_EXTENSIONS)) {
                continue;
            }

            return false;
        }

        return digit;
    }

    public static boolean startsWithNumber(Tokens source) {
        return isNumeric(source.asString().substring(0, 1));
    }

}
