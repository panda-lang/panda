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

package org.panda_lang.panda.utilities.commons.objects;

public class CharacterUtils {

    public static char TAB = '\t';
    public static char NO_BREAK_SPACE = '\u00A0';

    public static boolean belongsTo(char character, char... characters) {
        for (char c : characters) {
            if (c == character) {
                return true;
            }
        }

        return false;
    }

    public static boolean isWhitespace(char c) {
        return Character.isWhitespace(c) || c == NO_BREAK_SPACE;
    }

    public static int getIndex(char[] characters, char character) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == character) {
                return i;
            }
        }

        return -1;
    }

}
