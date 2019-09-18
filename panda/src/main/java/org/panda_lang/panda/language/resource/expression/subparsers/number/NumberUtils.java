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

package org.panda_lang.panda.language.resource.expression.subparsers.number;

import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.utilities.commons.CharacterUtils;

public final class NumberUtils {

    public static final char[] ALLOWED_CHARACTERS = new char[] { '.', '_', 'x' };

    public static final char[] NUMBER_EXTENSIONS = new char[] { 'b', 'B', 's', 'S', 'i', 'I', 'l', 'L', 'd', 'D', 'f', 'F' };

    private NumberUtils() { }

    public static boolean startsWithNumber(Snippet source) {
        if (source.isEmpty()) {
            return false;
        }

        return startsWithNumber(source.getFirst().getToken());
    }

    public static boolean startsWithNumber(Token token) {
        return token.getValue().length() != 0 && Character.isDigit(token.getValue().charAt(0));
    }

    public static boolean isNumeric(Snippet source) {
        return isNumeric(source.asSource());
    }

    public static boolean isNumeric(String content) {
        if (content == null) {
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

}
