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

package panda.interpreter.lexer;

import panda.interpreter.token.Snippet;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.source.PandaSource;
import panda.interpreter.resource.syntax.PandaSyntax;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.utilities.StringUtils;

public final class PandaLexerUtils {

    private static final PandaLexer LEXER = PandaLexer.of(new PandaSyntax()).enableSections().build();

    public static Snippet convert(String location, String source) {
        // TODO: module
        return LEXER.convert(new PandaSource(null, location, source));
    }

    public static String toString(Snippet source) {
        return toString(0, source);
    }

    private static String toString(int level, Snippet source) {
        StringBuilder content = new StringBuilder();

        for (TokenInfo representation : source) {
            if (representation.getType() != TokenTypes.SECTION) {
                content.append(representation.getToken().toString()).append(" ");
            }
            else {
                content.append(System.lineSeparator())
                        .append(StringUtils.buildSpace((level + 1) * 2))
                        .append(representation.getToken().toString())
                        .append(System.lineSeparator());
            }
        }

        return content.toString();
    }

}
