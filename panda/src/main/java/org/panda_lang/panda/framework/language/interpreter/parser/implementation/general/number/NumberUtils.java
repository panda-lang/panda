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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number;

import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;

public class NumberUtils {

    protected static final char[] NUMBER_EXTENSIONS = new char[]{ 'b', 'B', 's', 'S', 'i', 'I', 'l', 'L', 'd', 'D', 'f', 'F' };

    public static boolean isNumeric(TokenizedSource source) {
        return isNumber(source.asString());
    }

    public static boolean isNumber(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        boolean digit = false;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                digit = true;
                continue;
            }

            if (c == '.' || c == '_' || c == 'x') {
                continue;
            }

            if (CharacterUtils.belongsTo(c, NUMBER_EXTENSIONS)) {
                continue;
            }

            return false;
        }

        return digit;
    }

    public static boolean startsWithNumber(TokenizedSource source) {
        String str = source.asString();
        return isNumber(str.substring(0, 1));
    }

}
