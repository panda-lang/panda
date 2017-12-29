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

package org.panda_lang.panda.language.structure.general.expression.callbacks.number;

import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;

public class NumberUtils {

    private static final char[] NUMBER_EXTENSIONS = new char[]{ 'b', 'B', 's', 'S', 'i', 'I', 'l', 'L', 'd', 'D', 'f', 'F' };

    public static boolean isNumber(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        if (CharacterUtils.belongsTo(s.charAt(s.length() - 1), NUMBER_EXTENSIONS)) {
            s = s.substring(0, s.length() - 1);
        }

        if (s.isEmpty()) {
            return false;
        }

        boolean digit = false;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                digit = true;
            }
            else if (c != '.' && c != '_' && c != ',') {
                return false;
            }
        }

        return digit;
    }

}
