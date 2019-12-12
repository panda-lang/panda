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

package org.panda_lang.framework.language.resource.syntax.separator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.utilities.commons.ReflectionUtils;

import java.util.Collection;

/**
 * Default separators
 */
public final class Separators {

    public static final Separator SEMICOLON = new Separator(";", null);
    public static final Separator COMMA = new Separator(",", null);
    public static final Separator PERIOD = new Separator(".", null);
    public static final Separator VARARGS = new Separator("...", null);

    public static final Separator PARENTHESIS_RIGHT = new Separator(")", null);
    public static final Separator PARENTHESIS_LEFT = new Separator("(", PARENTHESIS_RIGHT);

    public static final Separator BRACE_RIGHT = new Separator("}", null);
    public static final Separator BRACE_LEFT = new Separator("{", BRACE_RIGHT);

    public static final Separator SQUARE_BRACKET_RIGHT = new Separator("]", null);
    public static final Separator SQUARE_BRACKET_LEFT = new Separator("[", SQUARE_BRACKET_RIGHT);

    private static final Collection<Separator> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Separators.class, Separator.class);
    }

    private Separators() { }

    public static @Nullable Separator valueOf(Token token) {
        for (Separator separator : values()) {
            if (separator.getValue().equals(token.getValue())) {
                return separator;
            }
        }

        return null;
    }

    public static @Nullable Separator valueOf(String str) {
        for (Separator separator : values()) {
            if (separator.getValue().equals(str)) {
                return separator;
            }
        }

        return null;
    }

    public static Separator[] getOpeningSeparators() {
        return new Separator[] { BRACE_LEFT, SQUARE_BRACKET_LEFT, PARENTHESIS_LEFT };
    }

    public static Separator[] values() {
        return VALUES.toArray(new Separator[0]);
    }

}

