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

package org.panda_lang.panda.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;

public class TokensUtils {

    public static boolean contains(Tokens source, Token... tokens) {
        for (Token token : tokens) {
            if (source.contains(token)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(@Nullable Tokens tokens) {
        return tokens == null || tokens.isEmpty();
    }

    public static String asString(@Nullable Tokens tokens) {
        return tokens != null ? tokens.asString() : null;
    }

    public static int getLine(@Nullable Tokens tokens) {
        return tokens != null ? tokens.getCurrentLine() : -2;
    }

}
