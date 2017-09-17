/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.core.interpreter.lexer.pattern;

import org.panda_lang.panda.framework.implementation.interpreter.token.PandaToken;
import org.panda_lang.panda.framework.language.interpreter.lexer.Syntax;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;

import java.util.Collection;

public class TokenPatternCompiler {

    private final TokenPatternBuilder builder;
    private final Syntax syntax;

    public TokenPatternCompiler(TokenPatternBuilder builder, Syntax syntax) {
        this.builder = builder;
        this.syntax = syntax;
    }

    public void compile(String expression) {
        String[] fragments = expression.split(" ");

        for (String fragment : fragments) {
            if (fragment.equals("+*")) {
                builder.hollow();
                continue;
            }

            Token token = getToken(fragment, syntax.getSeparators(), syntax.getOperators(), syntax.getKeywords(), syntax.getLiterals());

            if (token == null) {
                token = new PandaToken(TokenType.UNKNOWN, fragment);
            }

            builder.unit(token);
        }
    }

    @SafeVarargs
    protected final Token getToken(String fragment, Collection<? extends Token>... tokensCollections) {
        for (Collection<? extends Token> tokensCollection : tokensCollections) {
            for (Token token : tokensCollection) {
                if (!fragment.equals(token.getTokenValue())) {
                    continue;
                }

                return token;
            }
        }

        return null;
    }

}
