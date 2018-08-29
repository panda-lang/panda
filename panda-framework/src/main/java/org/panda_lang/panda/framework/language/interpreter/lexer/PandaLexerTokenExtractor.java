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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.PandaToken;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.Collection;

public class PandaLexerTokenExtractor {

    private final PandaLexer lexer;
    private final Syntax syntax;

    public PandaLexerTokenExtractor(PandaLexer lexer) {
        this.lexer = lexer;
        this.syntax = lexer.getSyntax();
    }

    protected boolean extract(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        while (tokenPreview.length() != 0) {
            tokenPreview = tokenPreview.trim();

            if (tokenPreview.isEmpty()) {
                return true;
            }

            Token token = extractToken(tokenPreview, syntax.getSeparators(), syntax.getOperators(), syntax.getKeywords(), syntax.getLiterals());

            if (token == null) {
                if (StringUtils.containsCharacter(tokenPreview, syntax.getSpecialCharacters())) {
                    return false;
                }

                token = new PandaToken(TokenType.UNKNOWN, tokenPreview);
            }

            lexer.getTokenizedLine().add(token);
            tokenBuilder.delete(0, token.getTokenValue().length());
            tokenPreview = tokenBuilder.toString();
        }

        return true;
    }

    @SafeVarargs
    protected final @Nullable Token extractToken(String tokenPreview, Collection<? extends Token>... tokensCollections) {
        for (Collection<? extends Token> tokensCollection : tokensCollections) {
            for (Token token : tokensCollection) {
                if (!tokenPreview.startsWith(token.getTokenValue())) {
                    continue;
                }

                if (token.getType() == TokenType.KEYWORD || token.getType() == TokenType.LITERAL) {
                    if (tokenPreview.length() > token.getTokenValue().length()) {
                        continue;
                    }
                }

                return token;
            }
        }

        return null;
    }

}
