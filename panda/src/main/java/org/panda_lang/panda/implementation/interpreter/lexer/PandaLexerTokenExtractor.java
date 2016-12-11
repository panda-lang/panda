/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.util.StringUtils;
import org.panda_lang.panda.implementation.interpreter.token.PandaToken;
import org.panda_lang.panda.implementation.syntax.SyntaxComposition;

import java.util.Collection;

public class PandaLexerTokenExtractor {

    private final PandaLexer lexer;
    private final SyntaxComposition syntaxComposition;

    public PandaLexerTokenExtractor(PandaLexer lexer) {
        this.lexer = lexer;
        this.syntaxComposition = lexer.getSyntaxComposition();
    }

    protected boolean extract(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        while (tokenPreview.length() != 0) {
            tokenPreview = tokenPreview.trim();

            if (tokenPreview.isEmpty()) {
                return true;
            }

            Token token = extractToken(tokenPreview, syntaxComposition.getSeparators(), syntaxComposition.getOperators(), syntaxComposition.getKeywords());

            if (token == null) {
                if (StringUtils.containsCharacter(tokenPreview, syntaxComposition.getSpecialCharacters())) {
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
    protected final Token extractToken(String tokenPreview, Collection<? extends Token>... tokensCollections) {
        for (Collection<? extends Token> tokensCollection : tokensCollections) {
            for (Token token : tokensCollection) {
                if (!tokenPreview.startsWith(token.getTokenValue())) {
                    continue;
                }

                return token;
            }
        }

        return null;
    }

}
