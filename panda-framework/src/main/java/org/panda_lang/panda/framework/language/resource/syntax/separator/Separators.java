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

package org.panda_lang.panda.framework.language.resource.syntax.separator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Collection;

/**
 * Default separators
 */
public class Separators {

    public static final Separator SEMICOLON = new Separator(";");
    public static final Separator COMMA = new Separator(",");
    public static final Separator PERIOD = new Separator(".");

    public static final Separator PARENTHESIS_LEFT = new Separator("(");
    public static final Separator PARENTHESIS_RIGHT = new Separator(")");

    public static final Separator BRACE_LEFT = new Separator("{");
    public static final Separator BRACE_RIGHT = new Separator("}");

    public static final Separator SQUARE_BRACKET_LEFT = new Separator("[");
    public static final Separator SQUARE_BRACKET_RIGHT = new Separator("]");

    //public static final Separator ANGLE_BRACKET_LEFT = new Separator("<");
    //public static final Separator ANGLE_BRACKET_RIGHT = new Separator(">");

    private static final Collection<Separator> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Separators.class, Separator.class);

        BRACE_LEFT.setOpposite(BRACE_RIGHT);
        SQUARE_BRACKET_LEFT.setOpposite(SQUARE_BRACKET_RIGHT);
        PARENTHESIS_LEFT.setOpposite(PARENTHESIS_RIGHT);
        //ANGLE_BRACKET_LEFT.setOpposite(ANGLE_BRACKET_RIGHT);
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

    public static Separator[] getOpeningSeparators() {
        return new Separator[] { BRACE_LEFT, SQUARE_BRACKET_LEFT, PARENTHESIS_LEFT/*, ANGLE_BRACKET_LEFT*/ };
    }

    public static Separator[] getClosingSeparators() {
        return new Separator[] { BRACE_RIGHT, SQUARE_BRACKET_RIGHT, PARENTHESIS_RIGHT, /*ANGLE_BRACKET_RIGHT*/ };
    }

    public static Separator[] values() {
        return VALUES.toArray(new Separator[0]);
    }

}

