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

package org.panda_lang.framework.language.interpreter.lexer;

import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.framework.language.resource.syntax.PandaSyntax;
import org.panda_lang.utilities.commons.StringUtils;

public class PandaLexerUtils {

    private static final PandaLexer LEXER = PandaLexer.of(new PandaSyntax()).enableSections().build();

    public static Snippet convert(String source) {
        return LEXER.convert(new PandaSource("runtime::PandaLexerUtils", source));
    }

    public static String toString(Snippet source) {
        return toString(0, source);
    }

    private static String toString(int level, Snippet source) {
        StringBuilder content = new StringBuilder();

        for (TokenRepresentation representation : source) {
            if (representation.getType() != TokenType.SECTION) {
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
