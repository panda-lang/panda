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

package org.panda_lang.panda.framework.design.interpreter.token.snippet;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.panda.framework.design.interpreter.token.Token;

public class SnippetUtils {

    public static boolean contains(Snippet source, Token... tokens) {
        for (Token token : tokens) {
            if (source.contains(token)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(@Nullable Snippet snippet) {
        return snippet == null || snippet.isEmpty();
    }

    public static String asString(@Nullable Snippet snippet) {
        return snippet != null ? snippet.asString() : null;
    }

    public static int getLine(@Nullable Snippet snippet) {
        return snippet != null ? snippet.getCurrentLocation().getLine() : SourceLocation.UNKNOWN_LOCATION;
    }

}
