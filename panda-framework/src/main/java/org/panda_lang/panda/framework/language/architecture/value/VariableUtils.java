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

package org.panda_lang.panda.framework.language.architecture.value;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.utilities.commons.CharacterUtils;

public class VariableUtils {

    private static final char[] ALLOWED_START = CharacterUtils.mergeArrays(
            CharacterUtils.LETTERS,
            CharacterUtils.arrayOf('$', '_')
    );

    private static final char[] ALLOWED = CharacterUtils.mergeArrays(
            ALLOWED_START,
            CharacterUtils.DIGITS
    );

    public static boolean isAllowedName(Token token) {
        if (token.getType() != TokenType.UNKNOWN) {
            return false;
        }

        if (!CharacterUtils.belongsTo(token.getTokenValue().charAt(0), ALLOWED_START)) {
            return false;
        }

        char[] chars = token.getTokenValue().toCharArray();

        for (int i = 1; i < token.getTokenValue().toCharArray().length; i++) {
            char c = chars[i];

            if (!CharacterUtils.belongsTo(c, ALLOWED)) {
                return false;
            }
        }

        return true;
    }

}