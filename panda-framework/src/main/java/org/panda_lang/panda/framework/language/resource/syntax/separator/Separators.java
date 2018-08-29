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

package org.panda_lang.panda.framework.language.resource.syntax.separator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;

/**
 * Default separators
 */
public class Separators {

    public static final Separator SEMICOLON = new Separator(";");

    public static final Separator COMMA = new Separator(",");

    public static final Separator PERIOD = new Separator(".");

    public static final Separator LEFT_PARENTHESIS_DELIMITER = new Separator("(");

    public static final Separator RIGHT_PARENTHESIS_DELIMITER = new Separator(")");

    public static final Separator LEFT_BRACE_DELIMITER = new Separator("{");

    public static final Separator RIGHT_BRACE_DELIMITER = new Separator("}");

    public static final Separator LEFT_BRACKET_DELIMITER = new Separator("[");

    public static final Separator RIGHT_BRACKET_DELIMITER = new Separator("]");

    private static final Separator[] VALUES = new Separator[9];

    static {
        VALUES[0] = SEMICOLON;
        VALUES[1] = COMMA;
        VALUES[2] = PERIOD;

        VALUES[3] = LEFT_PARENTHESIS_DELIMITER;
        VALUES[4] = RIGHT_PARENTHESIS_DELIMITER;

        VALUES[5] = LEFT_BRACE_DELIMITER;
        VALUES[6] = RIGHT_BRACE_DELIMITER;

        VALUES[7] = LEFT_BRACKET_DELIMITER;
        VALUES[8] = RIGHT_BRACKET_DELIMITER;

        LEFT_BRACE_DELIMITER.setOpposite(RIGHT_BRACE_DELIMITER);
        LEFT_BRACKET_DELIMITER.setOpposite(RIGHT_BRACKET_DELIMITER);
        LEFT_PARENTHESIS_DELIMITER.setOpposite(RIGHT_PARENTHESIS_DELIMITER);
    }

    public static @Nullable Separator valueOf(Token token) {
        for (Separator separator : values()) {
            if (separator.getTokenValue().equals(token.getTokenValue())) {
                return separator;
            }
        }

        return null;
    }

    public static @Nullable Separator valueOf(String str) {
        for (Separator separator : values()) {
            if (separator.getTokenValue().equals(str)) {
                return separator;
            }
        }

        return null;
    }

    public static Separator[] values() {
        return VALUES;
    }

}

