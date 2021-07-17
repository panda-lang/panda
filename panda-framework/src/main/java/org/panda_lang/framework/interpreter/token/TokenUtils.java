/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.token;

import org.jetbrains.annotations.Nullable;
import panda.utilities.ArrayUtils;

import java.util.Comparator;

public final class TokenUtils {

    public static final Comparator<Token> TOKEN_ORDER_COMPARATOR = (a, b) -> Integer.compare(b.getValue().length(), a.getValue().length());

    private TokenUtils() { }

    public static boolean hasName(Token token, String name) {
        return token.hasName() && token.getName().get().equals(name);
    }

    public static boolean contains(Token[] tokens, Token element) {
        for (Token token : tokens) {
            if (element.equals(token)) {
                return true;
            }
        }

        return false;
    }

    public static boolean contentEquals(@Nullable TokenInfo token, Token... variants) {
        if (token == null) {
            return ArrayUtils.contains(variants, null);
        }

        for (Token variant : variants) {
            if (token.contentEquals(variant)) {
                return true;
            }
        }

        return false;
    }

    public static boolean valueEquals(Token token, String... variants) {
        for (String variant : variants) {
            if (token.getValue().equals(variant)) {
                return true;
            }
        }

        return false;
    }

}
