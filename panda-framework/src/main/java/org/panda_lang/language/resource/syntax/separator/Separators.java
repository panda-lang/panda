/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.resource.syntax.separator;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.token.Token;

import java.util.ArrayList;
import java.util.Collection;

import static org.panda_lang.utilities.commons.collection.Lists.add;

/**
 * Default separators
 */
public final class Separators {

    private static final Collection<Separator> VALUES = new ArrayList<>();

    public static final Separator SEMICOLON = add(VALUES, new Separator(";", null));
    public static final Separator COMMA = add(VALUES, new Separator(",", null));
    public static final Separator PERIOD = add(VALUES, new Separator(".", null));
    public static final Separator VARARGS = add(VALUES, new Separator("...", null));

    public static final Separator PARENTHESIS_RIGHT = add(VALUES, new Separator(")", null));
    public static final Separator PARENTHESIS_LEFT = add(VALUES, new Separator("(", PARENTHESIS_RIGHT));

    public static final Separator BRACE_RIGHT = add(VALUES, new Separator("}", null));
    public static final Separator BRACE_LEFT = add(VALUES, new Separator("{", BRACE_RIGHT));

    public static final Separator SQUARE_BRACKET_RIGHT = add(VALUES, new Separator("]", null));
    public static final Separator SQUARE_BRACKET_LEFT = add(VALUES, new Separator("[", SQUARE_BRACKET_RIGHT));

    // public static final Separator ANGLE_BRACKET_RIGHT = add(VALUES, new Separator(">", null));
    // public static final Separator ANGLE_BRACKET_LEFT = add(VALUES, new Separator("<", ANGLE_BRACKET_RIGHT));

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
        return new Separator[] { BRACE_LEFT, SQUARE_BRACKET_LEFT, PARENTHESIS_LEFT, /* ANGLE_BRACKET_LEFT */ };
    }

    public static Separator[] values() {
        return VALUES.toArray(new Separator[0]);
    }

}

