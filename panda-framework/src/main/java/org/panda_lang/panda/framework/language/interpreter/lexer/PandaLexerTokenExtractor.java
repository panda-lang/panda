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

package org.panda_lang.panda.framework.language.interpreter.lexer;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.resource.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.PandaToken;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Collection;

class PandaLexerTokenExtractor {

    private final PandaLexerWorker worker;

    protected PandaLexerTokenExtractor(PandaLexerWorker worker) {
        this.worker = worker;
    }

    protected boolean extract(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();
        Syntax syntax = worker.getConfiguration().syntax;

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

            worker.addLineToken(token);
            tokenBuilder.delete(0, token.getValue().length());
            tokenPreview = tokenBuilder.toString();
        }

        return true;
    }

    @SafeVarargs
    protected final @Nullable Token extractToken(String tokenPreview, Collection<? extends Token>... tokensCollections) {
        String preparedTokenPreview = worker.getConfiguration().ignoringCase ? tokenPreview.toLowerCase() : tokenPreview;

        for (Collection<? extends Token> tokensCollection : tokensCollections) {
            for (Token token : tokensCollection) {
                String value = token.getValue();

                if (worker.getConfiguration().ignoringCase) {
                    value = value.toLowerCase();
                }

                if (!preparedTokenPreview.startsWith(value)) {
                    continue;
                }

                if (token.getType() == TokenType.KEYWORD || token.getType() == TokenType.LITERAL) {
                    if (tokenPreview.length() > value.length()) {
                        continue;
                    }
                }

                return token;
            }
        }

        return null;
    }

}
